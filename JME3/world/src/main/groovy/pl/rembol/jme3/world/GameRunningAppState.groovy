package pl.rembol.jme3.world

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.asset.AssetManager
import com.jme3.bullet.BulletAppState
import com.jme3.input.InputManager
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.FastMath
import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import com.jme3.post.FilterPostProcessor
import com.jme3.post.filters.FogFilter
import com.jme3.renderer.Camera
import com.jme3.renderer.ViewPort
import com.jme3.scene.Node
import com.jme3.shadow.DirectionalLightShadowRenderer
import com.jme3.system.AppSettings
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.context.support.GenericApplicationContext
import pl.rembol.jme3.world.building.Building
import pl.rembol.jme3.world.building.toolshop.Toolshop
import pl.rembol.jme3.world.player.PlayerService
import pl.rembol.jme3.world.rabbit.Rabbit
import pl.rembol.jme3.world.resources.ResourceType
import pl.rembol.jme3.world.resources.deposits.FruitBush
import pl.rembol.jme3.world.save.SaveState
import pl.rembol.jme3.world.terrain.Terrain

@TypeChecked
@CompileStatic
class GameRunningAppState extends AbstractAppState {

    private BulletAppState bulletAppState;

    private DirectionalLight directional;

    private boolean nightDayEffect = false;

    int frame = 200;

    private AppSettings settings;

    private ApplicationContext applicationContext;

    public GameRunningAppState(AppSettings settings) {
        this.settings = settings;
    }

    @Override
    public void update(float tpf) {
        frame++;

        if (nightDayEffect) {
            directional.setDirection(
                    new Vector3f(
                            (float) Math.sin(FastMath.TWO_PI / 400.0 * frame),
                            (float) Math.sin(FastMath.TWO_PI / 400.0 * frame) - 0.5,
                            (float) Math.cos(FastMath.TWO_PI / 400.0 * frame))
                            .normalize());
        }

    }

    @Override
    public void cleanup() {
        ConfigurableApplicationContext.class.cast(applicationContext).close();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        SimpleApplication simpleApp = (SimpleApplication) app;

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        // bulletAppState.setDebugEnabled(true);

        ConfigurableApplicationContext parentApplicationContext = initializeParentApplicationContext(simpleApp);

        String[] contexts = ["/app-ctx.xml"]
        applicationContext = new ClassPathXmlApplicationContext(contexts, parentApplicationContext);

        initLightAndShadows(app.getViewPort());
        Terrain terrain = applicationContext.getBean(Terrain.class);
        PlayerService playerService = applicationContext
                .getBean(PlayerService.class);

        SaveState load = SaveState.load("save.xml");
        terrain.init(load.getTerrain());
        playerService.loadPlayers(load.getPlayers());
        applicationContext.getBean(UnitRegistry.class).load(load.getUnits());

        Building toolshop = applicationContext.getAutowireCapableBeanFactory()
                .createBean(Toolshop.class).init(new Vector2f(15, -20));
        toolshop.setOwner(playerService.getActivePlayer());
        playerService.getActivePlayer().addResource(ResourceType.FOOD, 100);

        applicationContext.getAutowireCapableBeanFactory()
                .createBean(Rabbit.class).init(new Vector2f(30, 0));
        applicationContext.getAutowireCapableBeanFactory()
                .createBean(Rabbit.class).init(new Vector2f(30, 10));
        applicationContext.getAutowireCapableBeanFactory()
                .createBean(Rabbit.class).init(new Vector2f(40, 10));
        applicationContext.getAutowireCapableBeanFactory()
                .createBean(Rabbit.class).init(new Vector2f(40, 0));

        applicationContext.getAutowireCapableBeanFactory()
                .createBean(FruitBush.class).init(new Vector2f(35, -20));

        // new SaveState(terrain.save(), playerService.savePlayers(),
        // applicationContext.getBean(UnitRegistry.class).save())
        // .save("default.xml");

    }

    private ConfigurableApplicationContext initializeParentApplicationContext(
            SimpleApplication simpleApp) {
        ConfigurableApplicationContext parentApplicationContext = new GenericApplicationContext();

        parentApplicationContext.getBeanFactory()
                .registerSingleton(AssetManager.class.getSimpleName(),
                simpleApp.getAssetManager());
        parentApplicationContext.getBeanFactory().registerSingleton(
                GameRunningAppState.class.getSimpleName(), this);
        parentApplicationContext.getBeanFactory().registerSingleton("guiNode",
                simpleApp.getGuiNode());
        parentApplicationContext.getBeanFactory().registerSingleton("rootNode",
                simpleApp.getRootNode());
        parentApplicationContext.getBeanFactory().registerSingleton(
                AppSettings.class.getSimpleName(), settings);
        parentApplicationContext.getBeanFactory().registerSingleton(
                Camera.class.getSimpleName(), simpleApp.getCamera());
        parentApplicationContext.getBeanFactory()
                .registerSingleton(InputManager.class.getSimpleName(),
                simpleApp.getInputManager());
        parentApplicationContext.getBeanFactory().registerSingleton(
                BulletAppState.class.getSimpleName(), bulletAppState);
        parentApplicationContext.getBeanFactory().registerSingleton(
                SimpleApplication.class.getSimpleName(), simpleApp);

        parentApplicationContext.refresh();

        return parentApplicationContext;
    }

    private void initLightAndShadows(ViewPort viewPort) {
        directional = new DirectionalLight();
        directional.setDirection(new Vector3f(-2f, -10f, -5f).normalize());
        directional.setColor(ColorRGBA.White.mult(0.7f));
        applicationContext.getBean("rootNode", Node.class)
                .addLight(directional);
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(0.3f));
        applicationContext.getBean("rootNode", Node.class).addLight(ambient);

        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(
                applicationContext.getBean(AssetManager.class), SHADOWMAP_SIZE,
                3);
        dlsr.setLight(directional);
        viewPort.addProcessor(dlsr);

        FilterPostProcessor fpp = new FilterPostProcessor(
                applicationContext.getBean(AssetManager.class));

        FogFilter fog = new FogFilter();
        fog.setFogColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        fog.setFogDistance(400f);
        fog.setFogDensity(1.5f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
    }

}