package engine.ui;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

public class State extends JPanel {
	private static final long serialVersionUID = 2543001409713254719L;
	private ArrayList<UIEntity> entities = new ArrayList<UIEntity>();
	private Game parent;
	private Layout layout;
	
	public State setLayout(Layout layout) {
		this.layout = layout;
		return this;
	}
	
	public Layout getStateLayout() {
		return layout;
	}

	public State() {
		super();
		int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize(width, height);
	}
	
	public void clear() {
		entities.clear();
		if(layout!=null)
			layout.clear();
	}
	
	public void draw(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
	}
	
	public void drawFront(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		for(UIEntity entity : new ArrayList<UIEntity>(entities))
			entity.drawFront(g);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
	}
		
	@Override
	public void paint(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
		draw(g);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		for(UIEntity entity : new ArrayList<UIEntity>(entities))
			entity.draw(g);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
		drawFront(g);
	}
	
	public synchronized void process(double dt, Inputs inputs) {
		for(UIEntity entity : new ArrayList<UIEntity>(entities))
			entity.process(dt, inputs);
	}
	
	public State add(UIEntity entity) {
		if(entity==null)
			return this;
		if(layout!=null)
			layout.apply(entity);
		//entities.remove(entity);
		entities.add(entity);
		return this;
	}

	public void setParent(Game parent) {
		this.parent = parent;
	}
	
	public Game getParent() {
		return parent;
	}
}