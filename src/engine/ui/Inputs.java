package engine.ui;

public class Inputs {
	public int mouseX;
	public int mouseY;
	public boolean mousePressed;
	public Character keyPressed;
	
	public boolean mouseInRectangle(int x, int y, int dx, int dy) {
		return x<=mouseX && y<=mouseY && x+dx>=mouseX && y+dy>=mouseY;
	}

	public Inputs copy() {
		Inputs ret = new Inputs();
		ret.mouseX = mouseX;
		ret.mouseY = mouseY;
		ret.mousePressed = mousePressed;
		ret.keyPressed = keyPressed;
		return ret;
	}
}
