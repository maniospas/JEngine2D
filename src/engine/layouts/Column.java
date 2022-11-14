package engine.layouts;

import engine.ui.Group;
import engine.ui.Layout;
import engine.ui.UIEntity;

public class Column extends Layout {
	private double dx, dy;
	private double pos;
	private Group group;
	private int elements;
	public Column(Group group, int elements) {
		dx = group.getWidth();
		dy = group.getHeight()/elements;
		pos = group.getHeight()-dy*elements/2;
		this.group = group;
		this.elements = elements;
	}
	
	@Override
	public UIEntity apply(UIEntity entity) {
		entity.setBounds(0, pos+2, dx, dy-2);
		pos += dy;
		return entity;
	}

	@Override
	public void clear() {
		dx = group.getWidth();
		dy = group.getHeight()/elements;
		pos = group.getHeight()-dy*elements/2;
	}
	
}
