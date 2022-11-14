package engine.textures;

import java.awt.Graphics;
import java.awt.Image;

public class Animation {
	protected double progress;
	protected double speed;
	protected int currentGridX;
	protected int currentGridY;
	protected int gridWidth;
	protected int gridHeight;
	private boolean finished;
	private Image image;
	
	public Animation(Texture texture, int gridWidth, int gridHeight, double speed) {
		this.image = texture.image;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.speed = speed;
		restart();
	}
	
	public Animation restart() {
		progress = 0;
		currentGridX = 0;
		currentGridY = 0;
		finished = false;
		return this;
	}
	
	public Animation process(double dt) {
		if(finished)
			return this;
		progress += dt*speed;
		while(progress >= 1) {
			progress -= 1;
			currentGridX += 1;
		}
		while(currentGridX>=gridWidth && gridWidth>0) {
			currentGridX -= gridWidth;
			currentGridY += 1;
		}
		while(currentGridY>=gridHeight && currentGridY>0)  {
			finished = true;
			currentGridY -= currentGridY;
		}
		return this;
	}

	public void draw(Graphics g, int x, int y, int dx, int dy, int direction) {
		if(image==null || finished)
			return;
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		g.drawImage(image, x, y, x+dx, y+dy,
					width*currentGridX/gridWidth, height*currentGridY/gridHeight, 
					width*(currentGridX+1)/gridWidth, height*(currentGridY+1)/gridHeight, null);
	}

	public final void draw(Graphics g, double x, double y, double dx, double dy) {
		draw(g, (int)Math.round(x), (int)Math.round(y), (int)Math.round(dx), (int)Math.round(dy), 1);
	}

	public final void draw(Graphics g, double x, double y, double dx, double dy, int direction) {
		draw(g, (int)Math.round(x), (int)Math.round(y), (int)Math.round(dx), (int)Math.round(dy), direction);
	}
	
	public boolean isFinished() {
		return finished;
	}
}
