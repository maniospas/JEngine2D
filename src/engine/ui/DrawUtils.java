package engine.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class DrawUtils {
	private static HashMap<String, Shape> shapes = new HashMap<String, Shape>();

	public static void drawOutlined(Graphics g, String str, int x, int y) {
		if(str.isEmpty())
			return;
		g.drawString(str, x, y);
		/*
		Color fillColor = g.getColor();
	    Color outlineColor = Color.black;
        Graphics2D g2 = (Graphics2D) g;
		Shape textShape = shapes.get(str);
        Stroke originalStroke = g2.getStroke();
        RenderingHints originalHints = g2.getRenderingHints();
        
		if(textShape==null) {
	        GlyphVector glyphVector = g.getFont().createGlyphVector(g2.getFontRenderContext(), str);
	        shapes.put(str, textShape = glyphVector.getOutline());
		}

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        
        AffineTransform at = new AffineTransform();
        at.setToIdentity();
        at.translate(x, y);
        textShape = at.createTransformedShape(textShape);

        
        g2.setColor(fillColor);
        g2.fill(textShape); // fill the shape
        
        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(textShape); // draw outline

        // reset to original settings after painting
        g2.setColor(fillColor);
        g2.setStroke(originalStroke);
        g2.setRenderingHints(originalHints);*/
	}
}
