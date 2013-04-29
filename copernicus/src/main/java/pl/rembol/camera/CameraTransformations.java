package pl.rembol.camera;

import org.apache.commons.lang3.mutable.MutableDouble;

public class CameraTransformations {

	public static CameraTransformation rotate(MutableDouble rotation) {
		return new CameraRotation(rotation);
	}
	
	public static CameraTransformation tilt(MutableDouble tilt) {
		return new CameraTilt(tilt);
	}
	
	public static CameraTransformation moveForward(MutableDouble distance) {
		return new CameraMoveForward(distance);
	}
	
	public static CameraTransformation moveBackward(MutableDouble distance) {
		return new CameraMoveBackward(distance);
	}
	
}
