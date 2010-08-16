package de.steffenvogel.balls.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.VolatileImage;
import java.util.Observable;
import java.util.Observer;

import de.steffenvogel.balls.model.Ball;
import de.steffenvogel.balls.model.Barrier;
import de.steffenvogel.balls.model.Hole;
import de.steffenvogel.balls.model.Level;
import de.steffenvogel.util.Vector2d;

public class Field extends Component implements Runnable, Observer {

	private static final long serialVersionUID = 1L;
	public static final int VIRTUAL_RESOLUTION = 100;

	private Level level;
	
	VolatileImage backBuffer = null;

	/**
	 * This is the default constructor
	 */
	public Field(Level level) {
		super();
		
		this.level = level;
		this.setSize(level.size.toDimension());
	}

    void createBackBuffer() {
		if (backBuffer != null) {
		    backBuffer.flush();
		    backBuffer = null;
		}
		backBuffer = createVolatileImage(getWidth(), getHeight());
    }

	
	public void run() {
		System.out.println("render thread started");
		
		while (true) {
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void drawBall(Graphics g, Ball ball, boolean drawVec) {
		g.setColor(ball.color);
		g.fillOval(vPixel2Pixel(ball.position.x - ball.size),
				vPixel2Pixel(ball.position.y - ball.size),
				vPixel2Pixel(2 * ball.size), vPixel2Pixel(2 * ball.size));

		if (drawVec) {
			drawVector(g, ball.position, ball.orientation, true);
		}
	}

	private static int vPixel2Pixel(long vPixel) {
		return Math.round(vPixel / VIRTUAL_RESOLUTION);
	}

	private static long pixel2vPixel(int vPixel) {
		return vPixel * VIRTUAL_RESOLUTION;
	}

	public static Point vector2Point(Vector2d vec) {
		return new Point(vPixel2Pixel(vec.x), vPixel2Pixel(vec.y));
	}

	public static Vector2d point2Vector(Point point) {
		return new Vector2d(pixel2vPixel(point.x), pixel2vPixel(point.y));
	}

	public void paint(Graphics g) {
		if (backBuffer == null) {
		    createBackBuffer();
		}
		do {
		    int valCode = backBuffer.validate(getGraphicsConfiguration());
		    if (valCode == VolatileImage.IMAGE_RESTORED) { }
		    else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE || backBuffer.getWidth() != this.getWidth() || backBuffer.getHeight() != this.getHeight()) {
		    	createBackBuffer();
		    }
		    
		    Graphics2D gOff = (Graphics2D) backBuffer.getGraphics();
			gOff.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			gOff.setColor(Color.black);
			gOff.fillRect(0, 0, getWidth(), getHeight());
			
			for (Hole hole : level.holes) {
				gOff.setColor(Color.white);
				gOff.fillOval(vPixel2Pixel(hole.position.x - hole.size),
						vPixel2Pixel(hole.position.y - hole.size),
						vPixel2Pixel(2 * hole.size), vPixel2Pixel(2 * hole.size));
			}
		
			for (Barrier barrier : level.barriers) {
				drawVector(gOff, barrier.start.position, barrier.end.position
						.sub(barrier.start.position), false);
			}
		
			for (Ball ball : level.balls) {
				gOff.setColor(Color.white);
				drawBall(gOff, ball, false);
			}
			
			g.drawImage(backBuffer, 0, 0, this);
		} while (backBuffer.contentsLost());
	}

	private void drawVector(Graphics g, Vector2d pos, Vector2d vec,
			boolean arrow) {
		g.setColor(Color.white);
		g.drawLine(vPixel2Pixel(pos.x), vPixel2Pixel(pos.y), vPixel2Pixel(pos.x
				+ vec.x), vPixel2Pixel(pos.y + vec.y));
		
		// TODO draw Vectorarrows
		/*
		 * if (arrow) { int al = 1000; int ax =
		 * 
		 * g.drawLine(vPixel2Pixel(pos.x + vec.x), vPixel2Pixel(pos.y + vec.y),
		 * 0, 0); g.drawLine(vPixel2Pixel(pos.x + vec.x), vPixel2Pixel(pos.y +
		 * vec.y), 0, 0); }
		 */
	}

	@Override
	public void update(Observable o, Object arg) {
		Level level = (Level) o;
		this.setPreferredSize(level.size.toDimension());
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
