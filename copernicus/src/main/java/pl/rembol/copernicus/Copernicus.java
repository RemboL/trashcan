package pl.rembol.copernicus;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.vecmath.Vector3d;


import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import pl.rembol.camera.Camera;
import pl.rembol.commandcontrol.CommandControl;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.commands.AddCommand;
import pl.rembol.copernicus.commands.RemoveCommand;
import pl.rembol.copernicus.commands.RenameCommand;
import pl.rembol.copernicus.commands.SetColorCommand;
import pl.rembol.copernicus.commands.SetFocusCommand;
import pl.rembol.copernicus.commands.SetMassCommand;
import pl.rembol.copernicus.commands.SetPositionCommand;
import pl.rembol.copernicus.commands.SetRadiusCommand;
import pl.rembol.copernicus.commands.SetScaleDistanceCommand;
import pl.rembol.copernicus.commands.SetScaleSizeCommand;
import pl.rembol.copernicus.commands.SetScaleTimeCommand;
import pl.rembol.copernicus.commands.SetVelocityCommand;
import pl.rembol.copernicus.system.AstralSystem;

public class Copernicus {

    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    private Camera camera = new Camera(new Vector3d(0, 0, 0));
    private AstralSystem system = new AstralSystem();

    public void registerCommands() {
        CommandControl.registerCommand("add", new AddCommand(system));
        CommandControl.registerCommand("remove", new RemoveCommand(system));
        CommandControl.registerCommand("setFocus", new SetFocusCommand(system));
        CommandControl.registerCommand("rename", new RenameCommand(system));
        CommandControl.registerCommand("setColor", new SetColorCommand(system));
        CommandControl.registerCommand("setMass", new SetMassCommand(system));
        CommandControl.registerCommand("setRadius", new SetRadiusCommand(system));
        CommandControl.registerCommand("setPosition", new SetPositionCommand(system));
        CommandControl.registerCommand("setVelocity", new SetVelocityCommand(system));
        CommandControl.registerCommand("setScaleSize", new SetScaleSizeCommand(system));
        CommandControl.registerCommand("setScaleDistance", new SetScaleDistanceCommand(system));
        CommandControl.registerCommand("setScaleTime", new SetScaleTimeCommand(system));
    }
    /**
     * angle of quad rotation
     */
    float rotation = 0;
    /**
     * time at last frame
     */
    long lastFrame;
    /**
     * frames per second
     */
    int fps;
    /**
     * last fps time
     */
    long lastFPS;
    JFrame mainFrame;

    public void start() {

        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLayout(new BorderLayout(5, 5));

        final Canvas displayCanvas = new Canvas();
        displayCanvas.setSize(800, 600);
        displayCanvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = e.getComponent().getSize();
                newCanvasSize.set(size);
            }
        });
        displayCanvas.setFocusable(true);

        JTextField consoleField = new JTextField("");
        consoleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
                    JTextField consoleField = (JTextField) e.getComponent();
                    String command = consoleField.getText();
                    try {
                        CommandControl.invoke(command);
                    } catch (CommandException ex) {
                        Logger.getLogger(Copernicus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    consoleField.setText("");
                }
            }
        });

        mainFrame.getContentPane().add(displayCanvas, BorderLayout.CENTER);
        mainFrame.getContentPane().add(consoleField, BorderLayout.SOUTH);

        try {
            Display.setParent(displayCanvas);
            Display.setResizable(true);

        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        mainFrame.pack();
        mainFrame.setVisible(true);
        try {
            Display.create();

        } catch (LWJGLException ex) {
            Logger.getLogger(Copernicus.class.getName()).log(Level.SEVERE, null, ex);
        }

        camera.setZoomOut(10);

        registerCommands();

        initGL(); // init OpenGL
        lastFPS = getTime(); // call before loop to initialise fps timer
        try {
            CommandControl.invoke("runScript(solar.script)");
        } catch (CommandException ex) {
            Logger.getLogger(Copernicus.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (!Display.isCloseRequested()) {
            update();
            renderGL();

            Display.update();
            Display.sync(60); // cap fps to 60fps
        }

        Display.destroy();
    }

    public void update() {
        int dx = Mouse.getDX();
        int dy = Mouse.getDY();

        system.tick();

        if (Mouse.isButtonDown(1)) {
            camera.setCameraRotation(camera.getCameraRotation() + dx);
            camera.setCameraTilt(camera.getCameraTilt() - dy);
        }

        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            dWheel /= Math.abs(dWheel);
            dWheel = -dWheel;
            camera.setZoomOut(camera.getZoomOut() * (1 + 0.1 * dWheel));
        }

        updateFPS(); // update FPS Counter
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public void initGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float fAspect = (float) Display.getWidth()
                / (float) Display.getHeight();
        GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        // GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glClearDepth(1.0);
        GL11.glLineWidth(2);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        FloatBuffer matSpecular;
        FloatBuffer lModelAmbient;

        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        lModelAmbient = BufferUtils.createFloatBuffer(4);
        lModelAmbient.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);

        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModelAmbient);

        FloatBuffer lightPosition;

        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(5.0f).put(5.0f).put(0f).put(1.0f).flip();

        FloatBuffer colorBuffer;

        colorBuffer = BufferUtils.createFloatBuffer(4);
        colorBuffer.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();

        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, colorBuffer);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, colorBuffer);
        GL11.glEnable(GL11.GL_LIGHT0);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);

    }

    public void renderGL() {

        tryResize();

        // Clear The Screen And The Depth Buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glLoadIdentity();
        camera.translateCameraPosition();

        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glColor3f(0.1f, 0.4f, 0.9f);
        system.draw(camera);

//        drawXZGrid(20);

    }

    public static void main(String[] argv) {
        Copernicus copernicus = new Copernicus();
        copernicus.start();
    }

    public void transpateCameraPosition() {
        camera.translateCameraPosition();
    }

    public void drawXZGrid(int size) {
        GL11.glLoadIdentity();
        camera.translateCameraPosition();

        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        GL11.glBegin(GL11.GL_LINES);
        for (int x = -size; x <= size; ++x) {
            GL11.glVertex3d(x, 0, -size);
            GL11.glVertex3d(x, 0, size);
        }

        for (int z = -size; z <= size; ++z) {
            GL11.glVertex3d(-size, 0, z);
            GL11.glVertex3d(size, 0, z);
        }
        GL11.glEnd();

    }

    public void tryResize() {
        Dimension newDim = newCanvasSize.getAndSet(null);

        if (newDim != null) {
            GL11.glViewport(0, 0, newDim.width, newDim.height);
        }
    }
}