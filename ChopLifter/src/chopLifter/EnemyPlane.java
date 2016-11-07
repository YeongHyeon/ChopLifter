package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class EnemyPlane extends GameObj {
	private Image[] img = new Image[2];
	private int uTurn;
	private int spin;
	private int turnPoint = 140;
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
		if (state == ST_ALIVE) {
			dy = 10;
			noTurn = true;
		}
	}

	void move() {
		spin++; // 프로펠러 회전용 변수 
		image = img[spin % 2]; 

		
		// ALIVE 상태에서는 좌우로 이동
		if (state == ST_ALIVE) {
			if (x > ChopLifter.FRAME_W - 140) {
				uTurn = 1;
			} else if (x < 140 && uTurn != 2) {
				uTurn = 2;
			}
			if (y >= ChopLifter.FRAME_H / 5 * 4) {
				blast();
			}

			if (uTurn == 0) {
				if (noTurn == false)
					width = tmpW;
				if (dx < 10) {
					dx += 0.5;
				}
			} else if (uTurn == 1) {
				if (noTurn == false)
					width = -tmpW;
				if (dx > -10) {
					dx -= 0.5;
				}
			} else if (uTurn == 2) {
				if (noTurn == false)
					width = tmpW;
				if (dx < 10) {
					dx += 0.5;
				}
			}

			x += dx;
			y += dy;
		}
		// BLAST 상태에서는 count 시간 후 DEATH로 설정
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