package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import engine.textures.Texture;
import engine.textures.TextureLoader;

public class FancyDraw {
	private static Font titleFont = StyleManager.deriveFont(Font.BOLD, 27);
	private static Font tooltipFont = StyleManager.deriveFont(Font.PLAIN, 20);
	private static Font smallFont = StyleManager.deriveFont(Font.PLAIN, 16);
	
	public static void fancyDraw(Graphics g, String tooltip, 
			int px, int py, int dx, boolean fromTop) {
		int height = fancyDraw(g, tooltip, px, py, dx, 0);
		if(!fromTop)
			py -= height;
		fancyDraw(g, tooltip, px, py, dx, height);
	}
	
	protected static int fancyDraw(Graphics g, String tooltip, 
			int px, int py, int dx, int dy) {
		int startpy = py;
		String[] lines = tooltip.split("\\n");
		g.setColor(new Color(0,0,0,200));
		if(dy!=0) {
			g.fillRoundRect(px, py, 3*dx-40, dy, dx/5, dx/5);
			g.setColor(Color.black);
			g.drawRoundRect(px, py, 3*dx-40, dy, dx/5, dx/5);
		}
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
					if(dy!=0)
						g.drawString(word, lineX, py);
					lineX += g.getFontMetrics().stringWidth(word+" ");
					word = "";
					Font prevFont = g.getFont();
					g.setFont(StyleManager.deriveFallbackFont(prevFont));
					if(dy!=0)
						g.drawString(""+c, lineX, py);
					lineX += g.getFontMetrics().stringWidth(""+c);
					g.setFont(prevFont);
				}
				else if(c=='[') {
					if(dy!=0)
						g.drawString(word, lineX, py);
					lineX += g.getFontMetrics().stringWidth(word);
					word = ""+c;
				}
				else if(c==']') {
					word += c;
					if(word.equals("[white]"))
						g.setColor(Color.white);
					else if(word.equals("[black]"))
						g.setColor(Color.black);
					else if(word.equals("[red]"))
						g.setColor(Color.red);
					else if(word.equals("[green]"))
						g.setColor(Color.green);
					else if(word.equals("[yellow]"))
						g.setColor(Color.yellow);
					else if(word.equals("[gray]"))
						g.setColor(Color.lightGray);
					else if(word.equals("[blue]"))
						g.setColor(Color.cyan);
					else if(word.equals("[small]")) 
						g.setFont(normalFont==tooltipFont?smallFont:tooltipFont);
					else if(word.equals("[normal]")) 
						g.setFont(normalFont);
					else {
						word = word.substring(1, word.length()-1);
						lineX = px + 5 + Integer.parseInt(word)*g.getFontMetrics().charWidth('x');
					}
					
					word = "";
				}
				else if(c==' ') {
					if(word.startsWith("{") && word.endsWith("}")) {
						Texture texture = TextureLoader.get(word.substring(1, word.length()-1));
						int lineHeight = g.getFontMetrics().getHeight()*4/5;
						if(dy!=0)
							texture.draw(g, lineX, py-lineHeight, lineHeight, lineHeight);
						lineX += lineHeight;
					}
					else {
						if(dy!=0)
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
					if(dy!=0)
						texture.draw(g, lineX, py-lineHeight, lineHeight, lineHeight);
				}
				else if(dy!=0)
					g.drawString(word, lineX, py);
			}
			if(word.endsWith(".") || normalFont==titleFont)
				py += 10;
			if(line.isEmpty())
				py -= 10;
			py += 23;
		}
		return py - startpy - 11;
	}
}
