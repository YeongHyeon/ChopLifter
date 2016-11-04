package chopLifter;

import java.awt.Color;

public class Util {
	public static double rand(int max) {
		return Math.random() * max;
	}

	public static double rand(double min, double max) { // min~max범위에서 랜덤값 생성.
		return min + (Math.random() * (max - min));
	}

	public static boolean prob100(double r) { // 랜덤생성된 값이 r보다 작을경우 true.
		return (Math.random() * 100) <= r;
	}

	public static Color randColor() { // 0~255사이의 랜덤색 배정.
		return randColor(0, 255);
	}

	public static Color randColor(int min, int max) { // 지정된 범위 내의 랜덤색 배정.
		int r = (int) rand(min, max);
		int g = (int) rand(min, max);
		int b = (int) rand(min, max);
		return new Color(r, g, b);
	}
	
	public static int randColorElement(int min, int max) {
		// TODO Auto-generated method stub
		return (int) rand(min, max);
	}
}