package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import engine.textures.Texture;
import engine.textures.TextureLoader;


public class Button extends UIEntity {
	private String text = null;
	private boolean isEntered = false;
	private Color outline = Color.black;
	private Color fill = Color.lightGray;
	private Color fillEntered = Color.white;
	private Color textColor = Color.black;
	private Color textColorEntered = Color.black;
	private Font font = null;
	private Runnable action = null;
	private Texture texture = null;
	private Texture textureEntered = null;
	private Texture icon = null;
	private int enteredSizeIncrease = 0;
	private boolean enabled = true;
	private boolean visible = true;
	private String tooltip = null;
	private double tooltipWidth = 88;
	
	public boolean isEntered() {
		return isEntered;
	}
	
	public Button setText(String text) {
		this.text = text;
		return this;
	}
	
	public Button setFont(Font font) {
		this.font = font;
		return this;
	}
	
	@Override
	public Button setBounds(double x, double y, double dx, double dy) {
		super.setBounds(x, y, dx, dy);
		if(dy>1)
			font = font==null?StyleManager.deriveFont(Font.PLAIN, dy*0.7):font.deriveFont(Font.PLAIN, (float)(dy*0.7));
		return this;
	}
	
	public Button setTooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}
	
	public Button setTooltipWidth(double tooltipWidth) {
		this.tooltipWidth = tooltipWidth;
		return this;
	}
	
	public Button setTextColor(Color textColor) {
		this.textColor = textColor;
		textColorEntered = textColor;
		return this;
	}
	
	public Button setTextColorEntered(Color textColorEntered) {
		this.textColorEntered = textColorEntered;
		return this;
	}
	
	public Button setIcon(Texture icon) {
		this.icon = icon;
		return this;
	}
	
	public Button setTexture(Texture texture) {
		this.texture = texture;
		textureEntered = texture;
		return this;
	}
	
	public Button setTextureEntered(Texture textureEntered) {
		this.textureEntered = textureEntered;
		return this;
	}

	public Button setFill(Color fill) {
		this.fill = fill;
		return this;
	}
	
	public Button setFillEntered(Color fillEntered) {
		this.fillEntered = fillEntered;
		return this;
	}

	public Button setOutline(Color outline) {
		this.outline = outline;
		return this;
	}
	
	public Button setAction(Runnable action) {
		this.action = action;
		return this;
	}
	
	public Button setEnteredSizeIncrease(int enteredSizeIncrease) {
		this.enteredSizeIncrease = enteredSizeIncrease;
		return this;
	}
	
	public void onClick(Inputs inputs) {
		inputs.mousePressed = false;
		if(action!=null) 
			action.run();
	}

	public void process(double dt, Inputs inputs) {
		isEntered = inputs!=null && inputs.mouseInRectangle((int)x, (int)y, (int)dx, (int)dy);
		if(isEntered && enabled && inputs.mousePressed && visible)
			onClick(inputs);
	}
	
	public void drawFront(Graphics g) {
		if(!visible)
			return;
		if(isEntered && tooltip!=null) {
			int x = (int)this.x-(isEntered?enteredSizeIncrease:0);
			int y = (int)this.y-(isEntered?enteredSizeIncrease:0);
			int dx = (int)(tooltipWidth+0.5);//this.dx+2*(isEntered?enteredSizeIncrease:0);
			//int dy = 80;//this.dy+2*(isEntered?enteredSizeIncrease:0);
			int px = x+(int)this.dx/2-dx*3/2+20;
			int py = y-10;
			FancyDraw.fancyDraw(g, tooltip, px, py, dx, false);
		}
	}
	
	public void draw(Graphics g) {
		if(!visible)
			return;
		int x = (int)this.x-(isEntered?enteredSizeIncrease:0);
		int y = (int)this.y-(isEntered?enteredSizeIncrease:0);
		int dx = (int)this.dx+2*(isEntered?enteredSizeIncrease:0);
		int dy = (int)this.dy+2*(isEntered?enteredSizeIncrease:0);
		Texture tex = isEntered?textureEntered:texture;
		if(tex!=null) 
			tex.draw(g, x, y, dx, dy);
		if(icon!=null) {
			int dim = Math.min(dx, dy);
			int border = dim/20+1;
			icon.draw(g, x+dx/2-dim/2+border, y+dy/2-dim/2+border, dim-2*border, dim-2*border);
		}
		Color color = isEntered?fillEntered:fill;
		if(color!=null) {
			g.setColor(color);
			g.fillRect(x, y, dx, dy);
		}
		if(text!=null && textColor!=null) {
			g.setColor(isEntered?textColorEntered:textColor);
			g.setFont(font);
			DrawUtils.drawOutlined(g, text, 
					x+dx/2-g.getFontMetrics().stringWidth(text)/2, 
					y+dy/2-g.getFontMetrics().getHeight()/2+g.getFontMetrics().getAscent());
		}
		if(outline!=null) {
			g.setColor(outline);
			g.drawRect(x, y, dx, dy);
		}
	}

	public Button setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public Button setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public boolean isVisible() {
		return visible;
	}

	public Button setSize(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	public String getText() {
		return text;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public Color getTextColor() {
		return textColor;
	}
}
