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
	
	public Animation pixelate(Animation animation) {
		return new Animation(pixelate(animation.getTexture()), 
				animation.getGridWidth(), 
				animation.getGridHeight(), 
				animation.getSpeed()).setProgress(animation.getProgress());
	}
	
	public static BufferedImage resize(Image img, int newW, int newH) { 
	  Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	  BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	  Graphics2D g2d = dimg.createGraphics();
	  g2d.drawImage(tmp, 0, 0, null);
	  g2d.dispose();
	  return dimg;
	}
	
	protected static double colorDistance(int r1, int g1, int b1, int r2, int g2, int b2) {
	    double[] lab1 = RGBtoCIELAB(r1, g1, b1);
	    double[] lab2 = RGBtoCIELAB(r2, g2, b2);
	    double dL = lab1[0] - lab2[0];
	    double da = lab1[1] - lab2[1];
	    double db = lab1[2] - lab2[2];
	    return Math.sqrt(dL * dL + da * da + db * db);
	}
	
	
	protected static double[] RGBtoCIELAB(int r, int g, int b) {
	    // Convert the RGB values to XYZ using the CIE RGB to XYZ conversion matrix
	    double[] XYZ = {
	        0.4124 * r + 0.3576 * g + 0.1805 * b,
	        0.2126 * r + 0.7152 * g + 0.0722 * b,
	        0.0193 * r + 0.1192 * g + 0.9505 * b
	    };

	    // Normalize the XYZ values using the white point of the sRGB color space
	    double Xn = 95.047;
	    double Yn = 100.000;
	    double Zn = 108.883;
	    double X = XYZ[0] / Xn;
	    double Y = XYZ[1] / Yn;
	    double Z = XYZ[2] / Zn;

	    // Convert the XYZ values to CIELAB
	    double fX, fY, fZ;
	    if (X > 0.008856) {
	        fX = Math.pow(X, 1.0 / 3.0);
	    } else {
	        fX = (7.787 * X) + (16.0 / 116.0);
	    }
	    if (Y > 0.008856) {
	        fY = Math.pow(Y, 1.0 / 3.0);
	    } else {
	        fY = (7.787 * Y) + (16.0 / 116.0);
	    }
	    if (Z > 0.008856) {
	        fZ = Math.pow(Z, 1.0 / 3.0);
	    } else {
	        fZ = (7.787 * Z) + (16.0 / 116.0);
	    }
	    double L = (116.0 * fY) - 16.0;
	    double a = 500.0 * (fX - fY);
	    double bl = 200.0 * (fY - fZ);

	    return new double[]{L, a, bl};
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
                	/*double distance = 
                			Math.abs(red-color.getRed())
                			+ Math.abs(green-color.getGreen())
                			+ Math.abs(blue-color.getBlue());*/
                	double distance = colorDistance(red, green, blue, color.getRed(), color.getGreen(), color.getBlue())
                			+ Math.abs(color.getAlpha()-alpha);
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
