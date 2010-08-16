package de.steffenvogel.balls.model;

import java.awt.Color;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.steffenvogel.balls.view.Field;
import de.steffenvogel.util.Vector2d;


public class Ball implements Renderable {
	public long size;					// radius
	
	public static int maxSize = 40 * Field.VIRTUAL_RESOLUTION;
	public static int minSize = 15 * Field.VIRTUAL_RESOLUTION;
	private static long minSpeed = 6 * Field.VIRTUAL_RESOLUTION;
	
	public double mass;
	public Vector2d orientation;		// bearing & speed
	public Vector2d position;			// position
	public Color color;
	
	public static long getMaxSpeed() {
		return (2 * Ball.minSize) - 1;
	}
	
	public Ball(Vector2d position, Vector2d orientation, long size, double mass, Color color) {
		this.position = position;
		this.orientation = orientation;
		this.size = size;
		this.color = color;
		this.mass = mass == 0 ? 1e10 : mass;
	}
	
	public Ball(Ball ball) {
		this.position = new Vector2d(ball.position);
		this.orientation = new Vector2d(ball.orientation);
		this.size = ball.size;
		this.color = ball.color;
		this.mass = ball.mass;
	}
	
	public Ball(Element xml) {
		this.size = new Integer(xml.getAttribute("size"));
		this.mass = new Double(xml.getAttribute("mass"));
		this.color = new Color(new Integer(xml.getAttribute("color")));
		this.position = new Vector2d((Element) xml.getElementsByTagName("position").item(0));
		this.orientation = new Vector2d((Element) xml.getElementsByTagName("orientation").item(0));
	}
	
	public void move(float steps) {
		position.x += Math.round(orientation.x * steps);
		position.y += Math.round(orientation.y * steps);
	}
	
	static public Ball random(Level level) {
		Random rnd = new Random();
		
		// speed
		long speed = rnd.nextLong();
		if (speed < 0)
			speed *= -1;
		if (speed >= (getMaxSpeed() - minSpeed))
			speed %= (getMaxSpeed() - minSpeed);
		
		speed += minSpeed;
		speed /= 3;
		
		// orientation
		Vector2d orientation = new Vector2d(rnd.nextInt(), rnd.nextInt());
		orientation = orientation.scMultp(speed / (double) orientation.length());
		
		// position
		Vector2d position = new Vector2d(Ball.maxSize + Math.round(rnd.nextDouble() * (level.size.width - 2 * Ball.maxSize)), Ball.maxSize + Math.round(rnd.nextDouble() * (level.size.height - 2 * Ball.maxSize)));
		long size = Ball.minSize + rnd.nextInt(Ball.maxSize - Ball.minSize);
		
		double mass = Math.PI * Math.pow(size, 2);
		Color color = Ball.getColorFromSize(size);
		
		return new Ball(position, orientation, size, mass, color);
	}
	
	public Element toXml(Document doc) {
		Element xml = doc.createElement("ball");
		xml.setAttribute("size", String.valueOf(size));
		xml.setAttribute("mass", String.valueOf(mass));
		xml.setAttribute("color", String.valueOf(color.getRGB()));
		xml.appendChild(position.toXml("position", doc));
		xml.appendChild(orientation.toXml("orientation", doc));
		
		return xml;
	}
	
	public static Color getColorFromSize(long size) {
		return Color.getHSBColor((size - Ball.minSize) / (float) Ball.maxSize, 1f, 1f);
	}
	
	public boolean checkCollision(Ball b) {
		if (position.distance(b.position) <= size + b.size && b != this) {
				return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkCollision(Barrier barrier) {
		Vector2d z = barrier.start.position.sub(position);
		Vector2d r = barrier.end.position.sub(barrier.start.position);
		
		double d = Math.sqrt(2*z.x*r.x*z.y*r.y + Math.pow(r.x, 2)*Math.pow(size, 2) - Math.pow(r.x, 2)*Math.pow(z.y, 2) - Math.pow(r.y, 2)*Math.pow(z.x, 2) + Math.pow(r.y, 2)*Math.pow(size, 2));
		
		double t1 = (-(z.Multp(r)) + d) / (float) r.sqLength();
		double t2 = -(z.Multp(r) + d) / (float) r.sqLength();
		
		if (((t1 > 0 && t1 < 1) || (t2 > 0 && t2 < 1)) && this != barrier.start && this != barrier.end) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void collide(Ball b) {
		Vector2d q = position.sub(b.position);
		Vector2d n = new Vector2d(-q.y, q.x);
		Vector2d p = orientation.sub(b.orientation).scMultp(mass);
		
		Vector2d pr = q.scMultp(q.Multp(p) / (double) q.sqLength());
		Vector2d pt = n. scMultp(n.Multp(p) / (double) n.sqLength());
		
		orientation = pt.add(pr.scMultp((mass - b.mass) / (mass + b.mass))).scMultp(1 / mass).add(b.orientation);
		b.orientation = pr.scMultp((2 * b.mass) / (mass + b.mass)).scMultp(1 / b.mass).add(b.orientation);
	}
	
	public void collide(Barrier barrier) {
		Vector2d q = barrier.end.position.sub(barrier.start.position);
		Vector2d n = new Vector2d(-q.y, q.x);
		Vector2d p = orientation;
		
		Vector2d pt = q.scMultp(q.Multp(p) / (double) q.sqLength());
		Vector2d pr = n.scMultp(n.Multp(p) / (double) n.sqLength());
		orientation = pt.sub(pr);
	}
	
	public void stop() {
		orientation.x = 0;
		orientation.y = 0;
	}
}
