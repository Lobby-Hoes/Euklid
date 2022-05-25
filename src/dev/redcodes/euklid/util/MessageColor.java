package dev.redcodes.euklid.util;

import java.awt.Color;

public enum MessageColor {

	SUCCESS(new Color(16, 163, 16)), INFORMATION(new Color(230, 146, 11)), FAILED(new Color(207, 34, 19)),
	GRAY(new Color(37, 37, 46)), BACKGROUND(new Color(59, 173, 210));

	private Color color;

	MessageColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

}
