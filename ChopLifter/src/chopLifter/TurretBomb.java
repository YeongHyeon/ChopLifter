package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class TurretBomb extends GameObj {
	private Image img;

	TurretBomb(Image img, int w, int h) {
		image = img;
		state = ST_DEATH;
		width = w;
		height = h;
	}

	// x, y 위치에서 mx, my 위치로 폭탄 발사
	void shot(double x, double y, double mx, double my) { // 폭탄의 좌표 : x,y
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = x;
			this.y = y;
			dx = (mx - x) / 50; // 폭탄과 우주선 사이의 x좌표 거리를 50등분하여 폭탄의 이동거리 좌표를 정해주었다
			dy = (my - y) / 50; // 폭탄과 우주선 사이의 y좌표 거리를 50등분하여 폭탄의 이동거리 좌표를
			// 정해주었다.

			double rate = dy / dx;

			if (Math.abs(dx) <= 2) { // 수직일때
				if (dy > 0)
					dy = 3;
				else
					dy = -3;
			} else {// 아닐때
				if (dx > 0) {
					dy = 5 * rate;
					dx = 5;
				} else if (dx < 0) {
					dy = -5 * rate;
					dx = -5;
				}
			}
		}
	}

	void blast() {
		state = ST_BLAST;
		blast_count = 10;
	}

	// 타이머에 의한 폭탄의 움직임 처리
	void move() {
		if (state == ST_ALIVE) {
			x += dx;
			y += dy;
			if (y < -height || ChopLifter.FRAME_H + height < y || x < -width) {
				state = ST_DEATH;
			}
			if (y >= ((ChopLifter.FRAME_H / 5 * 4) + (ChopLifter.FRAME_H / 5 / 2))) {
				blast();
			}
		} else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	// 폭탄 그리기
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
			drawSmallBlast(g);
		}
	}
}
