package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class EnemyPlane extends GameObj {
	private boolean uTurn;

	EnemyPlane(Image img, int w, int h) {
		image = img;
		state = ST_DEATH; // ó�� ���� �� Death���·� ������.
		tmpW = w;
		width = tmpW;
		height = h;
	}

	void birth(double heliX) { // �̵�����, �̵��ӵ�, ��߳���, ��ȯ������ ������.
		state = ST_ALIVE;
		x = -tmpW;
		dx = 0;
		y = Util.rand(70, ChopLifter.FRAME_H / 5 * 3);
		uTurn = false;
	}

	// ���� ���� ����
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void move() {
		// ALIVE ���¿����� �¿�� �̵�
		if (state == ST_ALIVE) {
			if (x > ChopLifter.FRAME_W / 3) {
				uTurn = true;
			}

			if (uTurn == false) {
				width = tmpW;
				if (dx < 10) {
					dx += 0.5;
				}
			} else {
				width = -tmpW;
				dx -= 0.5;
			}

			if (x < -tmpW || ChopLifter.FRAME_W + tmpW < x) {
				state = ST_DEATH;
			}

			x += dx;
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
			drawBlast(g);
		}
	}
}