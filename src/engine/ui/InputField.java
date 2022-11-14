package engine.ui;

import java.awt.Color;
import java.awt.Graphics;


public class InputField extends Button {
	private String defaultText = null;
	private double time = 0;
	private static InputField active = null;
	
	public boolean isFocused() {
		return active == this;
	}
	
	public InputField() {
		setText("");
	}
	
	public InputField setDefaultText(String text) {
		defaultText = text;
		return this;
	}
	
	@Override
	public void process(double dt, Inputs inputs) {
		time += dt;
		if(time>1)
			time -= 1;
		if(inputs.keyPressed!=null) {
			if(inputs.keyPressed=='\b') {
				if(getText().length()>0)
					setText(getText().substring(0, getText().length()-1));
			}
			else if(Character.isDefined(inputs.keyPressed))
				setText(getText()+inputs.keyPressed);
			inputs.keyPressed = null;
		}
		super.process(dt, inputs);
	}
	
	@Override
	public void draw(Graphics g) {
		String prevText = super.getText();
		Color color = super.getTextColor();
		if(prevText.isEmpty() && defaultText!=null && !isFocused()) {
			setText(defaultText);
			setTextColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/2));
		}
		else {
			//if(time>0.5)
			setText(prevText+"|");
			//else
			//	setText(prevText+" ");
		}
		super.draw(g);
		setText(prevText);
		setTextColor(color);
	}
	
	@Override
	public void onClick(Inputs inputs) {
		active = this;
		super.onClick(inputs);
	}
	
}
