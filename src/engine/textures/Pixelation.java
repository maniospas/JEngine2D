package engine.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Pixelation {
	private HashMap<Texture, Texture> pixelated = new HashMap<Texture, Texture>();
	private ArrayList<Color> palette = new ArrayList<Color>();
	private int scale = 64;
	
	public Pixelation setScale(int scale) {
		this.scale = scale;
		pixelated.clear();
		return this;
	}
	
	public ArrayList<Color> getPalette() {
		return palette;
	}
	
	public Pixelation() {
		/*int[] levels = {64, 128, 255};
		for(int level: levels) {
			palette.add(new Color(level, level, level));
			palette.add(new Color(level, 0, 0));
			palette.add(new Color(0, level, 0));
			palette.add(new Color(0, 0, level));
			for(int level2: levels) {
				palette.add(new Color(level, level2, 0));
				palette.add(new Color(level, level2, 0));
				palette.add(new Color(0, level, level2));
				palette.add(new Color(level, 0, level2));
			}
		}*/
		palette.add(Color.decode("#540e46"));
		palette.add(Color.decode("#821653"));
		palette.add(Color.decode("#a1234d"));
		palette.add(Color.decode("#b35054"));
		palette.add(Color.decode("#c28d6b"));
		palette.add(Color.decode("#cfaa80"));
		palette.add(Color.decode("#cfbba5"));
		palette.add(Color.decode("#c99e91"));
		palette.add(Color.decode("#bd726f"));
		palette.add(Color.decode("#944e87"));
		palette.add(Color.decode("#3f2457"));
		palette.add(Color.decode("#18051a"));
		palette.add(Color.decode("#13102e"));
		palette.add(Color.decode("#1e2745"));
		palette.add(Color.decode("#29465e"));
		palette.add(Color.decode("#458282"));
		palette.add(Color.decode("#649e91"));
		palette.add(Color.decode("#a7d694"));
		palette.add(Color.decode("#ebe5ab"));
		palette.add(Color.decode("#e0c892"));
		palette.add(Color.decode("#c7a379"));
		palette.add(Color.decode("#91745c"));
		palette.add(Color.decode("#5c3c2e"));
		palette.add(Color.decode("#2b171b"));
		palette.add(Color.decode("#120612"));
		palette.add(Color.decode("#211b3b"));
		palette.add(Color.decode("#31376b"));
		palette.add(Color.decode("#72a6c2"));
		palette.add(Color.decode("#8acfdb"));
		palette.add(Color.decode("#c3dfe0"));
		palette.add(Color.decode("#f2ffff"));
		palette.add(Color.decode("#a0b6b8"));
		palette.add(Color.decode("#8d9da1"));
		palette.add(Color.decode("#5f6470"));
		palette.add(Color.decode("#3b3947"));
		palette.add(Color.decode("#24212b"));
	}
	
	public Texture pixelate(Texture texture) {
		Texture ret = pixelated.get(texture);
		if(ret==null)
			pixelated.put(texture, ret = new Texture(this.pixelate(texture.getImage())));
		return ret;
	}
	
	public static BufferedImage resize(Image img, int newW, int newH) { 
	  Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	  BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	  Graphics2D g2d = dimg.createGraphics();
	  g2d.drawImage(tmp, 0, 0, null);
	  g2d.dispose();
	  return dimg;
	}
	
	public Image pixelate(Image input) {
		int minDim = Math.min(input.getWidth(null), input.getHeight(null));
		int oldWidth = input.getWidth(null);
		int oldHeight = input.getHeight(null);
		BufferedImage image = resize(input, input.getWidth(null)*scale/minDim, input.getHeight(null)*scale/minDim);
		for(int i=0;i<image.getWidth();i++)
			for(int j=0;j<image.getHeight();j++) {
				int RGBA = image.getRGB(i, j);
                int alpha = (RGBA >> 24) & 255;
                int red = (RGBA >> 16) & 255;
                int green = (RGBA >> 8) & 255;
                int blue = RGBA & 255;
                Color bestColor = null;
                double minDistance = Double.POSITIVE_INFINITY;
                if(alpha<0.5) {
                    image.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
                    continue;
                }
                for(Color color : palette) {
                	double distance = 
                			Math.abs(red-color.getRed())
                			+ Math.abs(green-color.getGreen())
                			+ Math.abs(blue-color.getBlue());
                	if(distance<minDistance) {
                		bestColor = color;
                		minDistance = distance;
                	}
                }
                image.setRGB(i, j, bestColor.getRGB());
			}
		return resize(image, oldWidth, oldHeight);
	}
}
