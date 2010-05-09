package cg.gl2d.model;

import java.awt.Color;

public class EditorColor {
	
	public float red;
	public float green;
	public float blue;
	
	public EditorColor() {
	}
	
	public EditorColor(Color c) {
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
	}

	public EditorColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

}
