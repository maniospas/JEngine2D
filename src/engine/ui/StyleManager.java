package engine.ui;

import java.awt.Font;

public class StyleManager {
	private static Font fallbackFont = new Font("TimesRoman", Font.PLAIN, 1);
	private static Font font = new Font("TimesRoman", Font.PLAIN, 1);
	
	public static Font deriveFallbackFont(Font font) {
		return fallbackFont.deriveFont(font.getStyle(), font.getSize());
	}
	public static Font deriveFallbackFont(int style, double size) {
		return fallbackFont.deriveFont(style, (float)size);
	}
	public static void setDefaultFont(Font font) {
		StyleManager.font = font;
	}
	public static Font deriveFont(int style, double size) {
		return font.deriveFont(style, (float)size);
	}
	public static Font deriveFont(int style, float size) {
		return font.deriveFont(style, size);
	}
}
