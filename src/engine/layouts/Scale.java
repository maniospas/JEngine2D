package engine.layouts;

import engine.ui.Layout;
import engine.ui.UIEntity;

public class Scale extends Layout {
	private double scaleX, scaleY;
	
	public Scale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	@Override
	public UIEntity apply(UIEntity entity) {
		if(entity.getWidth()<=1 && entity.getHeight()<=1)
			entity.setBounds((int)(entity.getX()*scaleX), 
							 (int)(entity.getY()*scaleY),
							 (int)(entity.getWidth()*scaleX),
							 (int)(entity.getHeight()*scaleY));
		return entity;
	}

	@Override
	public void clear() {
	}
	
}
