package cg.gl2d.model;

import java.awt.Color;

public class EditorColor {
	
	public float red;
	public float green;
	public float blue;
	
	public EditorColor() {
	}
	
	public EditorColor(Color c) {
		red = c.getRed() / 255;
		green = c.getGreen() / 255;
		blue = c.getBlue() / 255;
	}

	public EditorColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public Color toNativeColor() {
		return new Color((int)(red * 255.0), (int)(green * 255.0), (int)(blue * 255.0));
	}
	
	@Override
	public String toString() {
        return getClass().getName() + "[r=" + red + "; g=" + green + "; b=" + blue + "]";
	}

}
