package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class EnemyPlane extends GameObj {
	private Image[] img = new Image[2];
	private double speed;
	private int uTurn, spin, turnPoint = 140;
	private boolean noTurn = false;

	EnemyPlane(Image[] imgEnemyPlane, int w, int h) {
		img = imgEnemyPlane;
		state = ST_DEATH; // 처음 만들 때 Death상태로 생성함.
		tmpW = w;
		width = tmpW;
		height = h;
	}

	void birth() { // 이동방향, 이동속도, 출발높이, 전환지점을 지정함.
		state = ST_ALIVE;
		x = -tmpW;
		y = Util.rand(70, ChopLifter.FRAME_H / 5 * 3);
		speed = Util.rand(0.5, 2);
		dx = 0;
		dy = 0;
		uTurn = 0;
		image = img[0];
		noTurn = false;
	}

	// 폭발 상태 설정
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void fall() {
		state = ST_FALLING;
		dy = 10;
		noTurn = true;
	}

	void death() {
		state = ST_DEATH;
	}

	void move() {
		spin++; // 프로펠러 회전용 변수
		image = img[spin % 2];

		// ALIVE 상태에서는 좌우로 이동
		if (state == ST_ALIVE) {
			if (x > ChopLifter.FRAME_W - turnPoint) {
				uTurn = 1;
			} else if (x < turnPoint && uTurn != 2) {
				uTurn = 2;
			}

			if (uTurn == 0) {
				if (noTurn == false)
					width = tmpW;
				if (dx < 10) {
					dx += 1;
					dy += 0.05;
				}
			} else if (uTurn == 1) {
				if (noTurn == false)
					width = -tmpW;
				if (dx > -10) {
					dx -= 1;
					dy -= 0.05;
				}
			} else if (uTurn == 2) {
				if (noTurn == false)
					width = tmpW;
				if (dx < 10) {
					dx += 1;
					dy += 0.05;
				}
			}

			degree = (int) dx;
			x += dx;
			y += dy;
		}
		// BLAST 상태에서는 count 시간 후 DEATH로 설정
		else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		} else if (state == ST_FALLING) {
			x += dx;
			y += dy;
			if (y >= ChopLifter.FRAME_H / 5 * 4) {
				blast();
			}
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE || state == ST_FALLING) {
			drawTiltImage(g);
		} else if (state == ST_BLAST) {
			drawBlast(g);
		}
	}
}