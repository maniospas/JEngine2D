package engine.ui;

import java.awt.Graphics;

import engine.textures.Texture;

public class TextureView extends UIEntity {
	private Texture texture = null;

	public TextureView setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}
	public void process(double dt, Inputs inputs) {
	}
	public void draw(Graphics g) {
		if(texture!=null)
			texture.draw(g, (int)x, (int)y, (int)dx, (int)dy);
	}
	public void drawFront(Graphics g) {
	}
}
