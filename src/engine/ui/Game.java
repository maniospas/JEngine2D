package engine.ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game extends JFrame {
	private static final long serialVersionUID = -9098598635649971171L;
	private int refreshDelay = 15;
	private State state;
	private Thread refresh;
	private Inputs inputs = new Inputs();
	private boolean working = false;

	public Game() {
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
					long prevTime = System.nanoTime()/1000000;
					if(state!=null) {
						if(!working) {
							working = true;
							state.process(refreshDelay/1000., inputs);
							SwingUtilities.invokeLater(() -> { panel.repaint();});
							working = false;
						}
					}
					long time = System.nanoTime()/1000000;
					if(time<prevTime+refreshDelay)
						try {
							Thread.sleep(prevTime+refreshDelay-time);
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
