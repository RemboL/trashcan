package pl.rembol.camera;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.opengl.GL11;

public class CameraRotation implements CameraTransformation {

	private MutableDouble rotation;
	
	CameraRotation(MutableDouble rotation) {
		this.rotation = rotation;
	}
	
	CameraRotation(double rotation) {
		this.rotation = new MutableDouble(rotation);
	}
	
	@Override
	public void transform() {
		GL11.glRotated(rotation.getValue(), 0, 1, 0);
	}

}
