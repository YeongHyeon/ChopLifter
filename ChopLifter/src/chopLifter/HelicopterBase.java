package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class HelicopterBase extends GameObj {

	HelicopterBase(Image img, int w, int h) {
		image = img;
		state = ST_ALIVE;
		x = ChopLifter.FRAME_W - (w / 3);
		y = ChopLifter.FRAME_H / 5 * 4 + (h / 2);
		width = w;
		height = h;
	}

	// ���� ���� ����
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
		y += 3;
		int tmp = width;
		width = height;
		height = tmp;
	}

	void move() {
		// ALIVE ���¿����� �¿�� �̵�
		if (state == ST_ALIVE) {

		}
		// BLAST ���¿����� count �ð� �� DEATH�� ����
		else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
			drawImage(g);
		}
	}
}