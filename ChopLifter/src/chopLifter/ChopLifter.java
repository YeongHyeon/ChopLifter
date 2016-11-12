package chopLifter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
class ChopLifterComponent extends JComponent {

	public static int ST_TITLE = 0;
	public static int ST_GAME = 1;
	public static int ST_ENDING = 2;

	public static int TIME_SLICE = 50;
	public static int MAX_CLOUD = 15;
	public static int MAX_MOUNTAIN = 30;
	public static int MAX_MISSILE = 3;
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
	private Missile[] missile = new Missile[MAX_MISSILE];
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
	Image imgTitle;

	private int state; // 게임 상태
	private int score; // 점수
	private int life; // 라이프
	private int ani_count; // 애니메이션 카운터 0~19 반복

	private int boarder;

	ChopLifterComponent() {

		// �씠誘몄� �씫湲�
		try {
			imgCloud = ImageIO.read(new File("images/cloud.png"));
			imgMountain = ImageIO.read(new File("images/mountain.png"));
			imgHelicopter[0] = ImageIO.read(new File("images/helicopter1.png"));
			imgHelicopter[1] = ImageIO.read(new File("images/helicopter2.png"));
			imgEnemyPlane[0] = ImageIO.read(new File("images/enemyPlane1.png"));
			imgEnemyPlane[1] = ImageIO.read(new File("images/enemyPlane2.png"));
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
			imgTitle = ImageIO.read(new File("images/title.png"));
			System.out.println("ImageRead");
		} catch (IOException e) {
			System.out.println("IOException Exit Program");
			System.exit(-1);
		}
		// 게임 상태 초기화
		state = ST_TITLE;
		ani_count = 0;

		init();

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

	void init() {
		// 헬리콥터 생성
		heli = new Helicopter(imgHelicopter, 130, 50);
		helibase = new HelicopterBase(imgHeliBase, 300, ChopLifter.FRAME_H / 5);
		// 미사일 생성
		for (int i = 0; i < MAX_MISSILE; i++) {
			missile[i] = new Missile(imgMissile, 30, 15);
		}

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
	}

	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// 헬기 움직임
			heli.move();
			// ENDING 화면 우주선 폭발 처리
			ani_count = (ani_count + 1) % 20; // 0 .. 19 반복
			if (state == ST_ENDING) {
				if (ani_count == 0) {
					heli.blast();
				}
			}
			// 미사일 움직임
			for (Missile m : missile) {
				m.move();
			}

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
						if (state == ST_GAME) { // GAME 상태에서만 폭탄 투하
							for (Bomb b : bomb) {
								b.shot(ep.getX(), ep.getY(), heli.getX(), heli.getY());
								break;
							}
						}
				}
				// 적항공기와 헬기 충돌
				if (ep.getState() == EnemyPlane.ST_ALIVE && heli.getState() == Helicopter.ST_ALIVE) {
					if (heli.getBBox().intersects(ep.getBBox())) {
						heli.blast();
						ep.fall();
						life--; // 라이프 감소
						if (life == 0) {
							state = ST_ENDING; // 게임 종료
						}
						break;
					}
				}
				// 적항공기와 미사일 충돌
				for (Missile m : missile) {
					if (m.getState() == Missile.ST_ALIVE) {
						if (ep.getState() == EnemyPlane.ST_ALIVE) {
							if (ep.getBBox().intersects(m.getBBox())) {
								ep.fall();
								m.blast();
								score += 10; // 점수 증가
								break; // 하나 터지면 탈출
							}
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
							life--; // 라이프 감소
							if (life == 0) {
								state = ST_ENDING; // 게임 종료
							}
							break;
						}
					}
				}
			}

			// 터렛 움직임
			for (int i = 0; i < MAX_TURRET; i++) {
				turr[i].move();
				turr[i].setPosin(heli.getX());
				int index = shotestTurret();
				if (state == ST_GAME) {
					for (TurretBomb tb : turretbomb) {
						if (index >= 0) {
							if (turr[index].getState() == Turret.ST_ALIVE && tb.getState() == TurretBomb.ST_DEATH) {
								tb.shot(turr[index].getX(), turr[index].getY(), heli.getX(), heli.getY());
								break;
							}
						}
					}
				}
				if (turr[i].getState() == Turret.ST_ALIVE) {
					for (Missile m : missile) {
						if (turr[i].getBBox().intersects(m.getBBox())) {
							turr[i].blast();
							m.blast();
							score += 10; // 점수 증가
							break; // 하나 터지면 탈출
						}
					}
				}
			}

			// 터렛 폭탐 움직임
			for (TurretBomb tb : turretbomb) {
				tb.move();
				if (tb.getState() == TurretBomb.ST_ALIVE) {
					if (tb.getState() == Helicopter.ST_ALIVE) {
						if (heli.getBBox().intersects(tb.getBBox())) {
							heli.blast();
							tb.blast();
							life--; // 라이프 감소
							if (life == 0) {
								state = ST_ENDING; // 게임 종료
							}
							break;
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
					if (p.boarding(heli.getX(), heli.getY())) {
						boarder++;
						score += 10;
					}
				} else {
					p.move();
				}
			}

			// 기지에 도착하면 사람이 내림
			if (heli.isLanded()) {
				if (heli.getBBox().intersects(helibase.getBBox())) {
					if (boarder > 0) {
						boarder--;
						score += 100;
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
					shifting(heli.getDegree());
				}
			}
		} else if (heli.directionX == Helicopter.GO_RIGHT) {
			if (absX <= (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)) {
				// NOP
			} else {
				if (absX >= (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
					// NOP
				} else {
					shifting(heli.getDegree());
				}
			}
		}
		if (absX > (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)
				&& absX < (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
			heli.setFixX();
		}
	}

	void shifting(double s) {
		// 산 이동
		for (Mountain m : mountain) {
			m.setShift(s / 5 * 3);
		}
		// 적 항공기 이동
		for (EnemyPlane ep : enemyPlane) {
			ep.setShift(s);
		}
		// 적 항공기 폭탄 이동
		for (Bomb b : bomb) {
			b.setShift(s);
		}
		// 적 포탑 이동
		for (Turret t : turr) {
			t.setShift(s);
		}
		// 적 포탑 폭탄 이동
		for (TurretBomb tb : turretbomb) {
			tb.setShift(s);
		}
		// 사람 이동
		for (Person p : person) {
			p.setShift(s);
			p.setShiftInitX(s);
		}
		helibase.setShift(s);
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

			if (state == ST_TITLE) {
				if (code == KeyEvent.VK_SPACE) { // 게임 시작
					state = ST_GAME;
					score = 0;
					life = 3;
					heli.startHelicopter(); // 게임 시작
					for (EnemyPlane ep : enemyPlane) {
						ep.death();
					}
				}
			} else if (state == ST_GAME) {
				if (code == KeyEvent.VK_UP) {
					heli.moveUp();
					// System.out.println("UP");
				} else if (code == KeyEvent.VK_DOWN && !heli.isLanding()) {
					heli.moveDown();
					// System.out.println("DOWN");
				} else if (code == KeyEvent.VK_LEFT && !heli.isLanded()) {
					if (heli.getDegree() <= 0 && heli.getAbsoluteX() > ChopLifter.Left_End_X + 120) {
						heli.moveLeft();
						for (Missile m : missile) {
							m.setInertia();
						}
						// System.out.println("LEFT");
					}
				} else if (code == KeyEvent.VK_RIGHT && !heli.isLanded()) {
					if (heli.getDegree() >= 0 && heli.getAbsoluteX() < ChopLifter.Right_End_X - 120) {
						heli.moveRight();
						for (Missile m : missile) {
							m.setInertia();
						}
						// System.out.println("RIGHT");
					}
				} else if (e.getKeyChar() == 'z') {
					for (Missile m : missile) {
						if (m.getState() == Missile.ST_DEATH) {
							m.shot(heli.getX(), heli.getY(), heli.width, 0, heli.getDegree());
							break;
						}
					}
				} else if (e.getKeyChar() == 'x' && !heli.isLanded()) {
					for (Missile m : missile) {
						if (m.getState() == Missile.ST_DEATH) {
							m.shot(heli.getX(), heli.getY(), heli.width, 1, heli.getDegree());
							break;
						}
					}
				}
			} else if (state == ST_ENDING) {
				if (code == KeyEvent.VK_ENTER) {
					state = ST_TITLE; // 제목 화면으로 이동
					init();
				}
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
		g.setColor(new Color(255, 255, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 220, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 10, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 200, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 30, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 188, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 60, ChopLifter.FRAME_W, ChopLifter.FRAME_H);

		for (EnemyPlane ep : enemyPlane) {
			ep.draw(g);
		}

		for (Turret t : turr) {
			t.draw(g);
		}

		for (Bomb b : bomb) {
			b.draw(g);
		}

		for (TurretBomb tb : turretbomb) {
			tb.draw(g);
		}

		for (Person p : person) {
			p.draw(g);
		}

		helibase.draw(g);
		for (Missile m : missile) {
			m.draw(g);
		}

		heli.draw(g);
		// 상태별 문자 출력
		if (state == ST_TITLE) {
			int zoom = Math.abs(ani_count - 10); // 10 .. 0 .. 9 반복
			g.drawImage(imgTitle, (ChopLifter.FRAME_W - 549) / 2 - zoom, (ChopLifter.FRAME_H - 175) / 2 - zoom,
					549 + zoom * 2, 175 + zoom * 2, null);
			if (ani_count < 10) { // 10 x 50msec = 500msec 주기
				Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				int width = metrics.stringWidth("PRESS SPACE KEY");
				g.setColor(Color.WHITE);
				g.drawString("PRESS SPACE KEY", (ChopLifter.FRAME_W - width) / 2, 430);
			}
		} else if (state == ST_GAME) {
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
			g.setFont(font);
			g.setColor(Color.BLACK);
			Graphics2D g2d;
			g2d = (Graphics2D) g;
			g2d.rotate(Math.toRadians(-(heli.degree)), 115 + (heli.degree), 100 + (heli.degree));
			g.drawString("SCORE: " + score, 200, 300);
			g.drawString("LIFE: " + life, 200, 330);
		} else if (state == ST_ENDING) {
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, 36);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			int width1 = metrics.stringWidth("YOUR SCORE IS  " + score);
			g.setColor(Color.WHITE);
			g.drawString("YOUR SCORE IS  " + score, (ChopLifter.FRAME_W - width1) / 2, 200);
			if (ani_count < 10) {// 10 x 50msec = 500msec 주기
				int width2 = metrics.stringWidth("PRESS ENTER KEY");
				g.drawString("PRESS ENTER KEY", (ChopLifter.FRAME_W - width2) / 2, 400);
			}
		}
	}

}

public class ChopLifter {
	public static int FRAME_W = 1000;
	public static int FRAME_H = 600;
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