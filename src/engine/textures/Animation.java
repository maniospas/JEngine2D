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
	private double cropx1 = 0;
	private double cropx2 = 0;
	private double cropy1 = 0;
	private double cropy2 = 0;
	private Texture texture;
	
	public Animation(Texture texture, int gridWidth, int gridHeight, double speed) {
		this.texture = texture;
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
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getGridWidth() {
		return gridWidth;
	}
	
	public int getGridHeight() {
		return gridHeight;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public Animation setProgress(double progress) {
		this.progress = progress;
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
	
	public Animation setCrop(double x1, double y1, double x2, double y2) {
		cropx1 = x1;
		cropx2 = x2;
		cropy1 = y1;
		cropy2 = y2;
		return this;
	}
	
	public void draw(Graphics g, int x, int y, int dx, int dy, int direction) {
		Image image = texture.image;
		if(image==null || finished)
			return;
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		g.drawImage(image, x, y, x+dx, y+dy,
					width*currentGridX/gridWidth+(int)(width*cropx1/gridWidth), 
					height*currentGridY/gridHeight+(int)(height*cropy1/gridHeight), 
					width*(currentGridX+1)/gridWidth-(int)(width*cropx2/gridWidth), 
					height*(currentGridY+1)/gridHeight-(int)(height*cropy2/gridHeight), 
					null);
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
