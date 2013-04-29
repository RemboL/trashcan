/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.rembol.copernicus.system;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import pl.rembol.camera.Camera;

/**
 *
 * @author RemboL
 */
public class AstralObject {

    public static final double GRAVITATIONAL_CONSTANT = 6.674 * Math.pow(10, -20); // [km^3 kg^-1 s^-2]
    private Vector3d position = new Vector3d(0, 0, 0);
    private Vector3d velocity = new Vector3d(0, 0, 0);
    private String name = "";
    private double mass = 1;
    private double radius = 1;
    private Color color = Color.WHITE;
    private Vector3d force = new Vector3d(0, 0, 0);
    private AstralSystem system;

    public AstralObject(AstralSystem system) {
        this.system = system;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        Logger.getLogger(AstralObject.class.getName()).log(Level.INFO, "setting position of " + name + " to (" + position.x + ", " + position.y + ", " + position.z + ")");
        this.position = position;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void addForce(Vector3d force) {
        this.force.add(force);
    }

    public void addGravityInfluence(AstralObject that) {
        Vector3d direction = new Vector3d(0, 0, 0);
        direction.sub(that.getPosition(), this.getPosition());

        double distance = direction.length();

        direction.normalize();

        double force = GRAVITATIONAL_CONSTANT * (that.getMass() * this.getMass()) / Math.pow(distance, 2);

        Vector3d forceVector = new Vector3d(0, 0, 0);
        forceVector.set(direction);
        forceVector.scale(force);

        addForce(forceVector);
    }

    public void tick() {
        velocity.scaleAdd(1 / mass * system.scaleTime, force, velocity);
        force.set(0, 0, 0);

        position.scaleAdd(system.scaleTime, velocity, position);
    }

    public void draw(Camera camera) {
        camera.translateCameraPosition();


        double sphereRadius = Math.log10(radius) * system.scaleSize;

        GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);

        GL11.glTranslated(position.x * system.scaleDistance, position.y * system.scaleDistance, position.z * system.scaleDistance);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(velocity.x * system.scaleDistance, velocity.y * system.scaleDistance, velocity.z * system.scaleDistance);
        GL11.glEnd();


//        System.out.println(name + " is being drawn at " + (position.x * system.scaleDistance) + " " + (position.y * system.scaleDistance) + " " + (position.z * system.scaleDistance));


        Sphere sphere = new Sphere();
        sphere.draw((float) sphereRadius, 20, 15);
    }
}
