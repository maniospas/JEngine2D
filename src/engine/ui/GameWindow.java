package engine.ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JWindow;

public class GameWindow extends JWindow {
	private static final long serialVersionUID = -8413190977126381283L;
	private int refreshDelay = 15;
	private State state;
	private Thread refresh;
	private Inputs inputs = new Inputs();
	private boolean working = false;

	public GameWindow() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = -6603228155180321599L;
			@Override
			public void paint(Graphics g) {
				state.paint(g);
			}
		};
		setContentPane(panel);
		refresh = new Thread() {
			@Override
			public void run() {
				while(true) {
					if(state!=null) {
						if(!working) {
							working = true;
							state.process(refreshDelay/1000., inputs);
							panel.repaint();
							working = false;
						}
					}
					try {
						Thread.sleep(refreshDelay);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				inputs.keyPressed = e.getKeyChar();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				inputs.keyPressed = null;
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
				inputs.mousePressed = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
				inputs.mousePressed = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				inputs.mouseX = e.getX();
				inputs.mouseY = e.getY();
			}
		});
	}
	
	public final void run() {
		init();
		setVisible(true);
		refresh.start();
	}
	
	public void init() {
	}
	
	public final State getGameState() {
		return state;
	}
	
	public final void setState(State state) {
		this.state = state;
	}
}