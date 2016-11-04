package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Mountain extends GameObj {

	Mountain(Image img, double x) {
		image = img;
		init(x);
	}

	void init(double x) {
		this.x = x;
		y = Util.rand(ChopLifter.FRAME_H / 5 * 4 - 100, ChopLifter.FRAME_H / 5 * 4);
		width = (int) Util.rand(300, 500);
		height = (int) width / 300 * 237;
		dx = width / 500;
	}

	void move() {
	}

	void draw(Graphics g) {
		drawImage(g);
	}

	@Override
	void blast() {
		// TODO Auto-generated method stub
		
	}
}