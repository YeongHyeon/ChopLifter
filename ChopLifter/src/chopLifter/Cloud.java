package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

class Cloud {

	private double x, y;
	private double dx, width, height;
	private Image img;

	Cloud(Image img) {
		this.img = img;
		init();
	}

	void init() {
		x = Util.rand(ChopLifter.FRAME_W - 1);
		y = Util.rand(-40, ChopLifter.FRAME_H / 5 * 4);
		width = Util.rand(100, 300);
		height = width / 2;
		dx = width / 500;
	}

	void move() {
		x += dx;
		if (x > ChopLifter.FRAME_W + 100) {
			init();
			x = -Util.rand(ChopLifter.FRAME_W / 10);
		}
	}

	void draw(Graphics g) {
		g.drawImage(img, (int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height, null);
	}
}
