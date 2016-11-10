package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Bomb extends GameObj {
	private double dx, dy; // 폭탄의 단위 이동 거리 대각선으로 이동할 것이기 때문에 2개 방향이 필요
	private int tmpW, tmpH;

	Bomb(Image img, int w, int h) {
		image = img;
		state = ST_DEATH;
		tmpW = w;
		width = tmpW;
		tmpH = h;
		height = tmpH;
	}

	// x, y 위치에서 mx, my 위치로 폭탄 발사
	void shot(double enemyX, double enemyY, double shipX, double shipY) {
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = enemyX;
			this.y = enemyY;
			dx = (shipX - x) / 50;
			dy = (shipY - y) / 50;
			if(dx < 0){
				width = -tmpW;
			} else {
				width = tmpW;
			}
			if(dy < 0){
				height = -tmpH;
			} else {
				height = tmpH;
			}
		}
	}

	// 폭발 상태 설정
	void blast() {
		state = ST_DEATH;
		x = ChopLifter.FRAME_W;
		y = ChopLifter.FRAME_H;
	}

	void move() {
		if (state == ST_ALIVE) {
			x += dx;
			y += dy;
			if (y < -40 || ChopLifter.FRAME_H + 40 < y || x < -40 || ChopLifter.FRAME_W + 40 < x) { // 화면밖으로나가면 죽음
				state = ST_DEATH;
			}
		}
	}

	// 그리기
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		}
	}
}