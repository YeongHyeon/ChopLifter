package chopLifter;

import java.awt.*;

public class Turret extends GameObj {

	private Image[] img = new Image[2];
	private int heliX;
	private int poro_num;

	// 생성자
	Turret(Image[] imgTurret, double tx, int w, int h) {
		img = imgTurret;
		state = ST_ALIVE;
		this.x = tx;
		this.y = ChopLifter.FRAME_H / 5 * 4;
		width = w;
		height = h;
		image = img[0];
		poro_num = 0;
	}

	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void setPosin(double d) {
		if (d > x) {
			image = img[0];
		} else {
			image = img[1];
		}
	}
	
	void setPoro() {
		poro_num++;
	}
	
	int getPoro() {
		return poro_num;
	}

	void move() {
		if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0)
				state = ST_DEATH;
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE)
			drawImage(g);
		else if (state == ST_BLAST)
			drawBlast(g);
	}

}
