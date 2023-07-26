package engine.ui;

import java.awt.Graphics;
import java.util.ArrayList;

public class Group extends UIEntity {
	private ArrayList<UIEntity> entities = new ArrayList<UIEntity>();
	private Layout layout = null;
	
	public Group add(UIEntity entity) {
		if(layout!=null)
			layout.apply(entity);
		entities.add(entity);
		return this;
	}
	
	public void clear() {
		if(layout!=null)
			layout.clear();
		entities.clear();
	}
	
	public Group setLayout(Layout layout) {
		this.layout = layout;
		return this;
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	@Override
	synchronized public void process(double dt, Inputs inputs) {
		for(UIEntity entity : entities) {
			double prevX = entity.getX();
			double prevY = entity.getY();
			entity.setX(prevX+getX());
			entity.setY(prevY+getY());
			entity.process(dt, inputs);
			entity.setX(entity.getX()-getX());
			entity.setY(entity.getY()-getY());
		}
	}

	@Override
	synchronized public void draw(Graphics g) {
		for(UIEntity entity : entities) {
			double prevX = entity.getX();
			double prevY = entity.getY();
			entity.setX(prevX+getX());
			entity.setY(prevY+getY());
			entity.draw(g);
			entity.setX(prevX);
			entity.setY(prevY);
		}
	}

	@Override
	synchronized public void drawFront(Graphics g) {
		for(UIEntity entity : entities) {
			double prevX = entity.getX();
			double prevY = entity.getY();
			entity.setX(prevX+getX());
			entity.setY(prevY+getY());
			entity.drawFront(g);
			entity.setX(prevX);
			entity.setY(prevY);
		}
	}
}
