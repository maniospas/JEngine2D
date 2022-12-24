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
	private static Font titleFont = StyleManager.deriveFont(Font.BOLD, 27);
	private static Font tooltipFont = StyleManager.deriveFont(Font.PLAIN, 20);
	private static Font smallFont = StyleManager.deriveFont(Font.PLAIN, 16);
	
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
			int dx = 88;//this.dx+2*(isEntered?enteredSizeIncrease:0);
			int dy = 80;//this.dy+2*(isEntered?enteredSizeIncrease:0);
			String[] lines = tooltip.split("\\n");
			dy = 20*lines.length+dx/20;
			int px = x+(int)this.dx/2-dx*3/2+20;
			int py = y-3*dy/2-10;
			g.setColor(new Color(0,0,0,200));
			g.fillRoundRect(px, py, 3*dx-40, 3*dy/2, dx/5, dx/5);
			g.setColor(Color.black);
			g.drawRoundRect(px, py, 3*dx-40, 3*dy/2, dx/5, dx/5);
			py += dx/20;
			py += 22;
			for(String line : lines) {
				g.setColor(Color.white); 
				int lineX = px+5;
				if(line.startsWith("# ")) {
					line = line.substring(2);
					g.setFont(titleFont);
					lineX = px+(3*dx-40)/2-g.getFontMetrics().stringWidth(
							line.split("\\[")[0])/2;
				}
				else 
					g.setFont(tooltipFont);
				Font normalFont = g.getFont();
				
				//g.drawString(line, px+5, py);
				String word = "";
				for(int i=0;i<line.length();i++) {
					char c = line.charAt(i);
					if(c>=128) {//non-ascii
						g.drawString(word, lineX, py);
						lineX += g.getFontMetrics().stringWidth(word+" ");
						word = "";
						Font prevFont = g.getFont();
						g.setFont(StyleManager.deriveFallbackFont(prevFont));
						g.drawString(""+c, lineX, py);
						lineX += g.getFontMetrics().stringWidth(""+c);
						g.setFont(prevFont);
					}
					else if(c=='[') {
						g.drawString(word, lineX, py);
						lineX += g.getFontMetrics().stringWidth(word);
						word = ""+c;
					}
					else if(c==']') {
						word += c;
						if(word.equals("[white]"))
							g.setColor(Color.white);
						if(word.equals("[black]"))
							g.setColor(Color.black);
						if(word.equals("[red]"))
							g.setColor(Color.red);
						if(word.equals("[green]"))
							g.setColor(Color.green);
						if(word.equals("[yellow]"))
							g.setColor(Color.yellow);
						if(word.equals("[gray]"))
							g.setColor(Color.lightGray);
						if(word.equals("[blue]"))
							g.setColor(Color.cyan);
						if(word.equals("[small]")) 
							g.setFont(normalFont==tooltipFont?smallFont:tooltipFont);
						if(word.equals("[normal]")) 
							g.setFont(normalFont);
						
						word = "";
					}
					else if(c==' ') {
						if(word.startsWith("{") && word.endsWith("}")) {
							Texture texture = TextureLoader.get(word.substring(1, word.length()-1));
							int lineHeight = g.getFontMetrics().getHeight()*4/5;
							texture.draw(g, lineX, py-lineHeight, lineHeight, lineHeight);
							lineX += lineHeight;
						}
						else {
							g.drawString(word, lineX, py);
							lineX += g.getFontMetrics().stringWidth(word+" ");
						}
						word = "";
					}
					else
						word += c;
				}
				if(!word.isEmpty()) {
					if(word.startsWith("{") && word.endsWith("}")) {
						Texture texture = TextureLoader.get(word.substring(1, word.length()-1));
						int lineHeight = g.getFontMetrics().getHeight()*4/5;
						texture.draw(g, lineX, py-lineHeight, lineHeight, lineHeight);
					}
					else
						g.drawString(word, lineX, py);
				}
				if(word.endsWith(".") || normalFont==titleFont)
					py += 10;
				if(line.isEmpty())
					py -= 10;
				py += 23;
			}
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
