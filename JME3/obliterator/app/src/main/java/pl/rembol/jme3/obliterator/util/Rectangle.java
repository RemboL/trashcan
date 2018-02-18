package pl.rembol.jme3.obliterator.util;

public class Rectangle {

	int minX;
	int minY;
	int maxX;
	int maxY;

	public Rectangle(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

	}

	public boolean contains(float x, float y) {
		return minX <= x && x <= maxX && minY <= y && y <= maxY;
	}

	public float xPart(float x) {
		return (x - minX) / (maxX - minX) * 2f - 1f;
	}

	public float YPart(float y) {
		return (y - minY) / (maxY - minY) * 2f - 1f;
	}

	public int width() {
		return maxX - minX;
	}

	public int minX() {
		return minX;
	}

	public int minY() {
		return minY;
	}

	public int height() {
		return maxY - minY;
	}

}
