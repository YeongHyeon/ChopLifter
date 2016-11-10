package chopLifter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
class ChopLifterComponent extends JComponent {
	public static int TIME_SLICE = 50;
	public static int MAX_CLOUD = 15;
	public static int MAX_MOUNTAIN = 30;
	public static int MAX_ENEMY_PLANE = 3;
	public static int MAX_TURRET = 50;
	public static int MAX_BOMB = 3;
	public static int MAX_TURRETBOMB = 2;
	public static int MAX_PERSON = 100;

	private Timer t;
	private Cloud[] cloud = new Cloud[MAX_CLOUD];
	private Mountain[] mountain = new Mountain[MAX_MOUNTAIN];
	private Helicopter heli;
	private EnemyPlane[] enemyPlane = new EnemyPlane[MAX_ENEMY_PLANE];
	private Missile missile;
	private Bomb[] bomb = new Bomb[MAX_BOMB];
	private Turret[] turr = new Turret[MAX_TURRET];
	private TurretBomb[] turretbomb = new TurretBomb[MAX_TURRETBOMB];
	private Person[] person = new Person[MAX_PERSON];
	private HelicopterBase helibase;

	Image imgCloud, imgMountain, imgPlaneBomb, imgTurretBomb, imgHeliBase;
	Image[] imgHelicopter = new Image[2];
	Image[] imgEnemyPlane = new Image[2];
	Image[] imgMissile = new Image[2];
	Image[] imgTurret = new Image[2];
	Image[] imgPerson = new Image[3];

	private int boarder;

	ChopLifterComponent() {

		// �씠誘몄� �씫湲�
		try {
			imgCloud = ImageIO.read(new File("images/cloud.png"));
			imgMountain = ImageIO.read(new File("images/mountain.png"));
			// imgHelicopter[0] = ImageIO.read(new
			// File("images/helicopter1.png"));
			// imgHelicopter[1] = ImageIO.read(new
			// File("images/helicopter2.png"));
			// imgEnemyPlane[0] = ImageIO.read(new
			// File("images/enemyPlane1.png"));
			// imgEnemyPlane[1] = ImageIO.read(new
			// File("images/enemyPlane2.png"));
			imgHelicopter[0] = ImageIO.read(new File("images/ahpach1.png"));
			imgHelicopter[1] = ImageIO.read(new File("images/ahpach2.png"));
			imgEnemyPlane[0] = ImageIO.read(new File("images/enemyPlane.png"));
			imgEnemyPlane[1] = ImageIO.read(new File("images/enemyPlane.png"));
			imgMissile[0] = ImageIO.read(new File("images/missile1.png"));
			imgMissile[1] = ImageIO.read(new File("images/missile2.png"));
			imgPlaneBomb = ImageIO.read(new File("images/bomb.png"));
			imgTurret[0] = ImageIO.read(new File("images/TurretRight.png"));
			imgTurret[1] = ImageIO.read(new File("images/TurretLeft.png"));
			imgTurretBomb = ImageIO.read(new File("images/TurretBomb.png"));
			imgPerson[0] = ImageIO.read(new File("images/person1.png"));
			imgPerson[1] = ImageIO.read(new File("images/person2.png"));
			imgPerson[2] = ImageIO.read(new File("images/person3.png"));
			imgHeliBase = ImageIO.read(new File("images/helibase.png"));
			System.out.println("ImageRead");
		} catch (IOException e) {
			System.out.println("IOException Exit Program");
			System.exit(-1);
		}

		// 헬리콥터 생성
		// heli = new Helicopter(imgHelicopter, 130, 50);
		heli = new Helicopter(imgHelicopter, 130, 70);
		helibase = new HelicopterBase(imgHeliBase, 300, 150);
		// 미사일 생성
		missile = new Missile(imgMissile, 30, 15);

		// 구름 생성
		for (int i = 0; i < MAX_CLOUD; i++) {
			cloud[i] = new Cloud(imgCloud, i);
		}
		// 산 생성
		double mx = ChopLifter.Right_End_X;
		for (int i = 0; i < MAX_MOUNTAIN; i++) {
			mountain[i] = new Mountain(imgMountain, mx);
			mx -= Util.rand(50, ChopLifter.FRAME_W / 3);
		}

		// 적 비행기 생성
		for (int i = 0; i < MAX_ENEMY_PLANE; i++) {
			enemyPlane[i] = new EnemyPlane(imgEnemyPlane, 110, 50);
		}
		// 적 항공기 폭탄 생성
		for (int i = 0; i < MAX_BOMB; i++) {
			bomb[i] = new Bomb(imgPlaneBomb, 30, 30);
		}
		// 포탑 생성
		double tx = 200;
		for (int i = 0; i < MAX_TURRET; i++) {
			turr[i] = new Turret(imgTurret, tx, 60, 100);
			tx -= Util.rand(ChopLifter.FRAME_W / 3, ChopLifter.FRAME_W / 3 * 2);
		}
		// 적 포탑 폭탄 생성
		for (int i = 0; i < MAX_TURRETBOMB; i++) {
			turretbomb[i] = new TurretBomb(imgTurretBomb, 20, 20); // 폭탄 생성
		}

		// 사람 생성
		for (int i = 0; i < MAX_PERSON; i++) {
			person[i] = new Person(imgPerson, 30, 40);
		}

		// Handler
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		// Timer
		t = new Timer(TIME_SLICE, new TimerHandler());
		t.start();
		System.out.println("Timer Start");

		this.addKeyListener(new KeyHandler());
		this.requestFocus();
		System.out.println("KeyHandler Run");
	}

	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// 헬기 움직임
			heli.move();
			// 미사일 움직임
			missile.move();

			// 배경부
			for (Cloud c : cloud) {
				c.move();
			}
			for (Mountain m : mountain) {
				m.move();
			}

			// 적
			// 적 항공기
			for (EnemyPlane ep : enemyPlane) {
				// 적 항공기 탄생
				if (ep.getState() == EnemyPlane.ST_DEATH && Util.prob100(10)) {
					ep.birth();
				}
				// 적 항공기 폭탄 발사
				if (ep.getState() == EnemyPlane.ST_ALIVE && Util.prob100(1) && !heli.isLanded()) {
					if (ep.getY() <= heli.getY() && !heli.isLanding())
						for (Bomb b : bomb) {
							b.shot(ep.getX(), ep.getY(), heli.getX(), heli.getY());
							break;
						}
				}
				// 적항공기와 헬기 충돌
				if (ep.getState() == EnemyPlane.ST_ALIVE && heli.getState() == Helicopter.ST_ALIVE) {
					if (heli.getBBox().intersects(ep.getBBox())) {
						heli.blast();
						ep.fall();
						break;
					}
				}
				// 적항공기와 미사일 충돌
				if (missile.getState() == Missile.ST_ALIVE) {
					if (ep.getState() == EnemyPlane.ST_ALIVE) {
						if (ep.getBBox().intersects(missile.getBBox())) {
							ep.fall();
							missile.blast();
							break; // 하나 터지면 탈출
						}
					}
				}
				ep.move();
			}

			// 적 항공기 폭탄 움직임
			for (Bomb b : bomb) {
				b.move();
				if (b.getState() == Bomb.ST_ALIVE) {
					if (heli.getState() == Helicopter.ST_ALIVE) {
						if (heli.getBBox().intersects(b.getBBox())) {
							heli.blast();
							b.blast();
						}
					}
				}
			}

			// 터렛 움직임
			for (int i = 0; i < MAX_TURRET; i++) {
				turr[i].move();
				turr[i].setPosin(heli.getX());
				int index = shotestTurret();
				for (TurretBomb tb : turretbomb) {
					if (index >= 0) {
						if (turr[index].getState() == Turret.ST_ALIVE && tb.getState() == TurretBomb.ST_DEATH) {
							tb.shot(turr[index].getX(), turr[index].getY(), heli.getX(), heli.getY());
							break;
						}
					}
				}
				if (turr[i].getState() == Turret.ST_ALIVE) {
					if (turr[i].getBBox().intersects(missile.getBBox())) {
						turr[i].blast();
						missile.blast();
						break; // 하나 터지면 탈출
					}
				}
			}

			// 터렛 폭탐 움직임
			for (TurretBomb t : turretbomb) {
				t.move();
				if (t.getState() == TurretBomb.ST_ALIVE) {
					if (t.getState() == Helicopter.ST_ALIVE) {
						if (heli.getBBox().intersects(t.getBBox())) {
							heli.blast();
							t.blast();
						}
					}
				}
			}

			// 사람 움직임
			for (Person p : person) {
				for (Turret t : turr) {
					if (t.getState() == Turret.ST_DEATH) {
						if (p.getState() == Person.ST_DEATH && t.getPoro() < 10) {
							t.setPoro();
							p.birth(t.getX(), t.getY());
						}
					}
				}
				if (heli.isLanded() && boarder < 100 && heli.getX() >= p.initX - 100 && heli.getX() <= p.initX + 100) {
					boarder += p.boarding(heli.getX(), heli.getY());
				} else {
					p.move();
				}
			}

			// 기지에 도착하면 사람이 내림
			if (heli.isLanded()) {
				if (heli.getBBox().intersects(helibase.getBBox())) {
					if (boarder > 0) {
						boarder--;
					}
				}
			}

			// 배경 전환
			shiftBackGround();

			repaint();
		}
	}

	// 배경 전환 Method
	void shiftBackGround() {
		double absX = heli.getAbsoluteX();
		if (heli.directionX == Helicopter.GO_LEFT) {
			if (absX >= (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
				// NOP
			} else {
				if (absX <= (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)) {
					// NOP
				} else {
					// 산 이동
					for (Mountain m : mountain) {
						m.setShift(heli.getDegree() / 5 * 3);
					}
					// 적 항공기 이동
					for (EnemyPlane ep : enemyPlane) {
						ep.setShift(heli.getDegree());
					}
					// 적 항공기 폭탄 이동
					for (Bomb b : bomb) {
						b.setShift(heli.getDegree());
					}
					// 적 포탑 이동
					for (Turret t : turr) {
						t.setShift(heli.getDegree());
					}
					// 적 포탑 폭탄 이동
					for (TurretBomb t : turretbomb) {
						t.setShift(heli.getDegree());
					}
					// 사람 이동
					for (Person p : person) {
						p.setShift(heli.getDegree());
						p.setShiftInitX(heli.getDegree());
					}
					helibase.setShift(heli.getDegree());
				}
			}
		} else if (heli.directionX == Helicopter.GO_RIGHT) {
			if (absX <= (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)) {
				// NOP
			} else {
				if (absX >= (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
					// NOP
				} else {
					for (Mountain m : mountain) {
						m.setShift(heli.getDegree() / 5 * 3);
					}
					for (EnemyPlane ep : enemyPlane) {
						ep.setShift(heli.getDegree());
					}
					for (Bomb b : bomb) {
						b.setShift(heli.getDegree());
					}
					for (Turret t : turr) {
						t.setShift(heli.getDegree());
					}
					for (TurretBomb t : turretbomb) {
						t.setShift(heli.getDegree());
					}
					for (Person p : person) {
						p.setShift(heli.getDegree());
						p.setShiftInitX(heli.getDegree());
					}
					helibase.setShift(heli.getDegree());
				}
			}
		}
		if (absX > (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)
				&& absX < (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
			heli.setFixX();
		}
	}

	int shotestTurret() {
		double dist = 0;
		int index = -1;
		double minDist = Math
				.sqrt(Math.pow(heli.getX() - turr[0].getX(), 2) + Math.pow(heli.getY() - turr[0].getY(), 2));
		if (heli.getY() > 350) {
			minDist = ChopLifter.FRAME_W;
		} else {
			index = 0;
			System.out.println(heli.getY());
		}
		if (!heli.isLanded() && !heli.isLanding()) {
			for (int i = 1; i < MAX_TURRET; i++) {
				dist = Math.sqrt(Math.pow(heli.getX() - turr[i].getX(), 2) + Math.pow(heli.getY() - turr[i].getY(), 2));
				if (heli.getY() > 350) {
					if (dist < minDist) {
						minDist = dist;
						index = i;
					}
				}
			}
		}
		return index;
	}

	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			if (code == KeyEvent.VK_UP) {
				heli.moveUp();
				// System.out.println("UP");
			} else if (code == KeyEvent.VK_DOWN && !heli.isLanding()) {
				heli.moveDown();
				// System.out.println("DOWN");
			} else if (code == KeyEvent.VK_LEFT && !heli.isLanded()) {
				if (heli.getDegree() <= 0 && heli.getAbsoluteX() > ChopLifter.Left_End_X + 120) {
					heli.moveLeft();
					missile.setInertia();

					// System.out.println("LEFT");
				}
			} else if (code == KeyEvent.VK_RIGHT && !heli.isLanded()) {
				if (heli.getDegree() >= 0 && heli.getAbsoluteX() < ChopLifter.Right_End_X - 120) {
					heli.moveRight();
					missile.setInertia();
					// System.out.println("RIGHT");
				}
			} else if (e.getKeyChar() == 'z') {
				missile.shot(heli.getX(), heli.getY(), heli.width, 0, heli.getDegree());
			} else if (e.getKeyChar() == 'x' && !heli.isLanded()) {
				missile.shot(heli.getX(), heli.getY(), heli.width, 1, heli.getDegree());
			}
		}

	}

	public void paintComponent(Graphics g) {
		// 하늘
		g.setColor(new Color(0, 188, 255));
		g.fillRect(0, 0, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(0, 188, 240));
		g.fillRect(0, 100, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(0, 188, 225));
		g.fillRect(0, 200, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(0, 188, 210));
		g.fillRect(0, 300, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		// 구름
		for (Cloud c : cloud) {
			c.draw(g);
		}
		// 산
		for (Mountain m : mountain) {
			m.draw(g);
		}
		// 땅
		// g.setColor(new Color(255, 255, 81));
		// g.fillRect(0, ChopLifter.FRAME_H / 5 * 4, ChopLifter.FRAME_W,
		// ChopLifter.FRAME_H);
		// g.setColor(new Color(255, 220, 81));
		// g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 10, ChopLifter.FRAME_W,
		// ChopLifter.FRAME_H);
		// g.setColor(new Color(255, 200, 81));
		// g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 30, ChopLifter.FRAME_W,
		// ChopLifter.FRAME_H);
		// g.setColor(new Color(255, 188, 81));
		// g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 60, ChopLifter.FRAME_W,
		// ChopLifter.FRAME_H);
		g.setColor(new Color(115, 225, 62));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4, ChopLifter.FRAME_W, ChopLifter.FRAME_H);

		for (EnemyPlane ep : enemyPlane) {
			ep.draw(g);
		}

		for (Turret t : turr) {
			t.draw(g);
		}

		for (Bomb b : bomb) {
			b.draw(g);
		}

		for (TurretBomb t : turretbomb) {
			t.draw(g);
		}

		for (Person p : person) {
			p.draw(g);
		}

		helibase.draw(g);

		missile.draw(g);

		heli.draw(g);
	}
}

public class ChopLifter {
	public static int FRAME_W = 1000;
	public static int FRAME_H = 562;
	public static int Left_End_X = -2000;
	public static int Right_End_X = 1000;

	public static void main(String[] arg) {
		System.out.println("Start Program");
		JFrame f = new JFrame("ChopLifter");
		f.setSize(FRAME_W + 16, FRAME_H + 34);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChopLifterComponent sc = new ChopLifterComponent();
		f.add(sc);
		f.setVisible(true);
	}
}
