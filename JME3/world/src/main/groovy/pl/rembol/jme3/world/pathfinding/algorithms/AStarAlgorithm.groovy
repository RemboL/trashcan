package pl.rembol.jme3.world.pathfinding.algorithms;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked;
import org.springframework.context.ApplicationContext;
import pl.rembol.jme3.world.pathfinding.ClusterBorder;
import pl.rembol.jme3.world.pathfinding.PathfindingCluster;
import pl.rembol.jme3.world.pathfinding.Rectangle2f;
import pl.rembol.jme3.world.pathfinding.Vector2i;
import pl.rembol.jme3.world.pathfinding.paths.ComplexPath;
import pl.rembol.jme3.world.pathfinding.paths.IPath2i;
import pl.rembol.jme3.world.pathfinding.paths.Vector2iPath;
import pl.rembol.jme3.world.pathfinding.paths.VectorPath;

import java.util.*;
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream;

@TypeChecked
@CompileStatic
public class AStarAlgorithm {

    public static class AStarComparator implements Comparator<IPath2i> {

        private Rectangle2f target;

        public AStarComparator(Rectangle2f target) {
            this.target = target;
        }

        @Override
        public int compare(IPath2i path1, IPath2i path2) {
            return Float.compare(getDistance(path1), getDistance(path2));
        }

        private float getDistance(IPath2i path) {
            return target.distance(path.getLast()) + path.getLength();
        }

    }

    public static ComplexPath buildSectorPath(Map<ClusterBorder, Vector2iPath> startingPaths, Rectangle2f target,
                                              Map<ClusterBorder, VectorPath> targetPaths, int maxIterations) {

        AStarComparator comparator = new AStarComparator(target);

        TreeSet<ComplexPath> paths = new TreeSet<>(comparator);

        startingPaths.forEach({ ClusterBorder border, Vector2iPath path -> paths.add(new ComplexPath(path, border)) })

        int iteration = 0;
        Set<Vector2i> nodesVisited = new HashSet<>();
        Set<PathfindingCluster> clustersVisited = new HashSet<>();
        while (!paths.isEmpty() && iteration++ < maxIterations) {
            ComplexPath path = paths.pollFirst();

            if (nodesVisited.contains(path.last)) {
                continue;
            }
            nodesVisited.add(path.last);
            if (path.lastBorder) {
                clustersVisited.add(path.lastBorder.cluster);
            }

            ComplexPath finishedPath = targetPaths
                    .findAll({ it.key.middlePoint == path.last })?.collect({
                new ComplexPath(targetPaths.get(it.value).last, path)
            })?.find({ it })
            if (finishedPath) {
                return finishedPath
            }

            List<ComplexPath> newPaths = path.lastBorder.neighbors
                    .findAll({ !nodesVisited.contains(it.middlePoint) })
                    .collect({ new ComplexPath(it, path) })
                    .findAll({ !paths.contains(it) })

            List<ComplexPath> finishedPaths = newPaths.collectMany({
                ComplexPath newPath ->
                    targetPaths.keySet()
                            .findAll({ it.middlePoint == newPath.last })?.collect({
                        new ComplexPath(targetPaths.get(it).last, newPath)
                    })
            })
            if (finishedPaths) {
                return finishedPaths.first()
            }

            paths.addAll(newPaths)
        }
        return null;
    }

    public static Optional<VectorPath> buildUnitPath(Vector2f start, Rectangle2f target,
                                                     ApplicationContext applicationContext, int maxIterations,
                                                     Function<Vector2i, Boolean> isBlockFreeFunction) {

        if (!isBlockFreeFunction.apply(new Vector2i(start))) {
            return Optional.empty();
        }

        AStarComparator comparator = new AStarComparator(target);

        TreeSet<Vector2iPath> paths = new TreeSet<>(comparator);

        Vector2iPath startingPath = new Vector2iPath(start);
        paths.add(startingPath);

        int iteration = 0;
        Set<Vector2i> nodesVisited = new HashSet<>();
        while (!paths.isEmpty() && iteration++ < maxIterations) {
            Vector2iPath path = paths.pollFirst();

            if (nodesVisited.contains(path.last)) {
                continue;
            }
            nodesVisited.add(path.last);

            if (target.distance(path.last) == 0) {
                return Optional.of(new VectorPath(path, applicationContext));
            }

            List<Vector2iPath> newPaths = path.last.neighbors
                    .findAll({ canTraverse(isBlockFreeFunction, nodesVisited, it) })?.collect({
                new Vector2iPath(path, it.key)
            })?.findAll({ !paths.contains(it) })

            Vector2iPath finishedPath = newPaths?.findAll({ target.distance(it.last) == 0 })?.find({ it })

            if (finishedPath) {
                return Optional.of(new VectorPath(finishedPath, applicationContext))
            }

            paths.addAll(newPaths)

        }

        return Optional.empty();
    }

    private static boolean canTraverse(
            Function<Vector2i, Boolean> isBlockFreeFunction,
            Set<Vector2i> nodesVisited,
            Map.Entry<Vector2i, List<Vector2i>> neighborEntry) {
        return (isBlockFreeFunction.apply(neighborEntry.key)
                && !nodesVisited.contains(neighborEntry.key)
                && neighborEntry.value.stream().allMatch({ isBlockFreeFunction.apply(it) }));
    }

}
