package engine.ui;

import java.awt.Graphics;
import java.awt.Toolkit;

public abstract class UIEntity {
	protected double x, y, dx, dy;
	
	public UIEntity() {
	}
	
	public UIEntity setBounds(double x, double y, double dx, double dy) {
		if(x<0)
			x += Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		if(y<0)
			y += Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		return this;
	}
	
	public UIEntity setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public UIEntity setX(double x) {
		this.x = x;
		return this;
	}
	
	public UIEntity setY(double y) {
		this.y = y;
		return this;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getWidth() {
		return dx;
	}
	
	public double getHeight() {
		return dy;
	}
	
	public abstract void process(double dt, Inputs inputs);
	public abstract void draw(Graphics g);
	public abstract void drawFront(Graphics g);
}
