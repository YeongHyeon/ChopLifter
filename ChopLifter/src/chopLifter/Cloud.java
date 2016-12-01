package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

class Cloud {

	private double x, y;
	private double dx, width, height;
	private Image img;

	Cloud(Image img, int i) {
		this.img = img;
		init(i);
	}

	void init(int i) {
		// x = (-ChopLifter.FRAME_W) * (i);
		// y = ChopLifter.FRAME_H / 5 * 2;
		// width = ChopLifter.FRAME_W;
		// height = ChopLifter.FRAME_H / 5 * 4;
		// dx = 0.5;
		width = Util.rand(100, 300);
		height = width / 2;
		x = Util.rand(-width, ChopLifter.FRAME_W);
		y = Util.rand(-height, ChopLifter.FRAME_H / 5 * 4);
		dx = width / 500;
	}

	void move() {
		x += dx;
		// if (x > ChopLifter.FRAME_W - 16) {
		// init(2);
		// }
		if (x > ChopLifter.FRAME_W + width) {
			x = -width;
		}
	}

	void draw(Graphics g) {
		g.drawImage(img, (int) x, (int) (y - height / 2), (int) width, (int) height, null);
	}
}
