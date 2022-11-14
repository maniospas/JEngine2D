package engine.textures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	protected Image image;
	
	private static Image toCompatibleImage(Image image){
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();
		BufferedImage new_image = gfx_config.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return new_image;
	}
	
	public Texture(String path) {
		try {
			/*if(new File(path).exists())
				image = ImageIO.read(new File(path));
			else*/
			image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(path));
		}
		catch (Exception e) {
			System.err.println(path);
			e.printStackTrace();
			System.exit(1);
		}
		if(image!=null)
			image = toCompatibleImage(image);
	}
	
	public Image getImage() {
		return image;
	}
	
	public final void draw(Graphics g, int x, int y, int dx, int dy) {
		draw(g, x, y, dx, dy, 1);
	}
	
	public void draw(Graphics g, int x, int y, int dx, int dy, int direction) {
		if(direction==1)
			g.drawImage(image, x, y, x+dx, y+dy, 0, 0, image.getWidth(null), image.getHeight(null), null);
		else
			g.drawImage(image, x+dx, y, x, y+dy, 0, 0, image.getWidth(null), image.getHeight(null), null);
	}
}
