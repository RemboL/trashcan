package pl.rembol.jme3.world.resources.deposits;

import pl.rembol.jme3.world.resources.units.Log;
import pl.rembol.jme3.world.resources.units.ResourceUnit;
import pl.rembol.jme3.world.save.TreeDTO;
import pl.rembol.jme3.world.save.UnitDTO;

import com.jme3.math.Vector2f;

public class Tree extends ResourceDeposit {

    public float getWidth() {
        return 2f;
    }

    @Override
    public String getIconName() {
        return "tree";
    }

    @Override
    public UnitDTO save(String key) {
        return new TreeDTO(key, this);
    }

    @Override
    public void load(UnitDTO unit) {
        if (TreeDTO.class.isInstance(unit)) {
            init(new Vector2f(unit.getPosition().x, unit.getPosition().z));
            setHp(TreeDTO.class.cast(unit).getHp());
        }
    }

    @Override
    protected float getPhysicsRadius() {
        return 1.5f;
    }

    @Override
    protected String getModelFileName() {
        return "tree.blend";
    }

    @Override
    protected String getName() {
        return "Tree";
    }

    @Override
    protected RandomDirectionMode getRandomDirectionMode() {
        return RandomDirectionMode.WHOLE_CIRCLE;
    }

    @Override
    public ResourceUnit produceResource(int chopCounter) {
        return (ResourceUnit) new Log().init(applicationContext, getLocation(), chopCounter);
    }

}