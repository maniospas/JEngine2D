# JEngine2D
This is a graphics engine built on top of Graphics2D.
It is a bit slow but really easy to get into for simple games.

**Creator:** Emmanouil (Manios) Krasanakis<br>
**Contact:** maniospas@hotmail.com

## :zap: Quickstart

Your main class should override the *Game* class, instantiate and run its instance.
Game instances can switch between states with the *setState* command. The following
demonstrates ones simple game instantiation process.


```java
public class MyGame extends Game {

	public MyGame() {
		super();
		setState(new MyState());
	}
	
	public static void main(String[] args) {
		Sound.setVolume(1);
		new MyGame().run();
	}
}
```

Of course, it remains to define the first state class. Let's add some menu elements to
the state. Don't forget to put your assets (fonts, sounds, images) in your class hierarchy
so that they can be packaged into a *.jar* later and loaded with a class loader.


```java
public class MyState extends State {
	private static Font font;
	
	public MyState() {
		if(font==null) {
			try {
			font = Font.createFont(Font.TRUETYPE_FONT, Create.class.getClassLoader().getResourceAsStream("myfont.ttf")); // load a font here
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		engine.ui.StyleManager.setDefaultFont(fancyFont); // set the default font to be used by new components
		setLayout(new Scale(getWidth(), getHeight())); // set layout that maps coordinates to the range [0,1] on each dimension
		add(new PlainButton()
				.setAction(new Runnable() {
					@Override
					public void run() {
						// add what to do when clicking the button
					}
				})
				.setText("Click me!")
				.setBounds(.5, .75, .2, .08)
			);
	}
	
	@Override
	public void process(double dt, Inputs inputs) {
		super.process(dt, inputs);
		// process any game animation here
	}
}
``` 

## :brain: Useful components

The following class shows how to create a fancy button from textures.
It makes use of the engine's texture loader to retrieve the textures (respective files
are loaded only once).


```java

public class FancyButton extends Button {
	private static Color fancyColor = new Color(0, 0, 32);

	public FancyButton() {
		super();
		setFill(null);
		setFillEntered(null);
		setTextColor(fancyColor);
		setOutline(null);
	}
	
	@Override
	public FancyButton setBounds(double x, double y, double dx, double dy) {
		super.setBounds(x, y, dx, dy);
		if(getTexture()==null) {
			if(dx==dy) {
				setTexture(TextureLoader.get("res/button2.png"));
				setTextureEntered(TextureLoader.get("res/button2_over.png"));
			}
			else {
				setTexture(TextureLoader.get("res/button.png"));
				setTextureEntered(TextureLoader.get("res/button_over.png"));
			}
		}
		return this;
	}

	public FancyButton clear() {
		setFill(null);
		setFillEntered(null);
		setTextColor(fancyColor);
		setTexture(null);
		setTextureEntered(null);
		setOutline(null);
		return this;
	}

}
```
