package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class Label extends UIEntity {
	private String text = null;
	private Font font;
	private Color color = Color.darkGray;
	
	public Label setText(String text) {
		this.text = text;
		return this;
	}
	
	public String getText() {
		return text;
	}
	
	public Label(String text, Font font) {
		this.text = text;
		this.font = font;
	}

	@Override
	public void process(double dt, Inputs inputs) {
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.setFont(font);
		int py = 0;
		for(String line : text.split("\n")) {
			py += g.getFontMetrics().getHeight();
			g.drawString(line, 
					(int)(getX()+getWidth()/2)-g.getFontMetrics().stringWidth(line)/2, 
					(int)getY()+py);
		}
	}

	@Override
	public void drawFront(Graphics g) {
	}

	public Label setColor(Color color) {
		this.color = color;
		return this;
	}
	
}
