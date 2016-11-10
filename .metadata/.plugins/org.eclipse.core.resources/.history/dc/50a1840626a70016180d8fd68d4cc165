package chopLifter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Helicopter extends GameObj {
	private Image[] img = new Image[2];
	private int spin, tilt_counter;
	private double initY, landpoint, dy;
	private boolean landing;
	private double absX;

	Helicopter(Image[] imgHelicopter, int w, int h) {
		state = ST_ALIVE;
		img = imgHelicopter;
		tmpW = w;
		height = h;

		x = ChopLifter.FRAME_W - tmpW;
		absX = x;
		initY = ChopLifter.FRAME_H / 5 * 4 + 10;
		y = initY;
		dx = 0;
		dy = 0;

		landpoint = ChopLifter.FRAME_H / 5 * 4 - 100;
		landing = true;
		width = tmpW;
		image = img[0];
		directionX = GO_NEUTRAL;
	}

	// 폭발 상태 설정
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	private void setDegree() { // 기울임 설정 Method
		if (tilt_counter < 15) {
			tilt_counter++;
		}
	}

	int getDegree() {
		return degree;
	}

	void setFixX() {
		x = ChopLifter.FRAME_W / 2;
	}

	double getAbsoluteX() {
		return absX;
	}

	// 왼쪽으로 이동
	void moveLeft() {
		directionX = GO_LEFT;

		setDegree();
		width = tmpW;

		if (x >= tmpW) {
			dx = degree;
		}
	}

	// 오른쪽으로 이동
	void moveRight() {
		directionX = GO_RIGHT;

		setDegree();
		width = -tmpW;

		if (x < ChopLifter.FRAME_W - tmpW) {
			dx = degree;
		}
	}

	// 위로 이동
	void moveUp() {
		directionY = GO_UP;
		dy -= 1;
		if (dy < -5) {
			dy = -5;
		}
	}

	// 아래로 이동
	void moveDown() {
		directionY = GO_DOWN;
		if (!isLanding()) {
			dy = 3;
		}
	}

	// 착륙중인지 확인
	boolean isLanding() {
		return landing;
	}

	// 착륙했는지 확인
	boolean isLanded() {
		if (y >= initY) {
			return true;
		} else {
			return false;
		}
	}

	void move() {
		spin++; // 프로펠러 회전용 변수
		image = img[spin % 2];

		if (directionX == GO_LEFT) {
			degree = -tilt_counter;
		} else if (directionX == GO_RIGHT) {
			degree = tilt_counter;
		}

		if (tilt_counter > 0) { // 기울임
			tilt_counter--;
		}

		if (dx < 0 && x + dx < tmpW) {
			x = tmpW;
		} else if (dx > 0 && x + dx > ChopLifter.FRAME_W - tmpW) {
			x = ChopLifter.FRAME_W - tmpW;
		} else {
			x += dx;
		}

		if (dx < 0 && absX + dx < ChopLifter.Left_End_X + tmpW) {
			absX = ChopLifter.Left_End_X + tmpW;
		} else if (dx > 0 && absX + dx > ChopLifter.Right_End_X - tmpW) {
			absX = ChopLifter.Right_End_X - tmpW;
		} else {
			absX += dx;
		}

		if (dy < 0 && y + dy <= height) {
			y = height;
		} else if (dy > 0 && y + dy >= initY) {
			y = initY;
		}
		if (directionX == GO_NEUTRAL){
			y += dy;
			if (dy < 3) {
				dy += 0.5;
			}
		} 
		
		if (y >= landpoint && !isLanded() && directionY == GO_NEUTRAL) {
			landing = true;
			dy = Math.abs(initY - y) / 37;
			if (dy < 1) {
				dy = 1;
			}
		} else if (y < landpoint) {
			landing = false;
		}

		dx = degree;
		if (degree == 0) {
			directionX = GO_NEUTRAL;
		}

		// 터졌을때
		if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_ALIVE;
			}
		} else if (state == ST_BLAST) {
			blast_count--;
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			// drawImage(g);
			drawTiltImage(g);
		} else if (state == ST_BLAST) {
			if (blast_count % 2 == 0) {
				drawTiltImage(g);
			}
			drawBlast(g);
		}
	}
}