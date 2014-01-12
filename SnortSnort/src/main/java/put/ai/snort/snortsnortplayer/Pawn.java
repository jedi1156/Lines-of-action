package put.ai.snort.snortsnortplayer;

import put.ai.snort.game.Player.Color;

public class Pawn{
	int x;
	int y;
	Color color;

	public Pawn( int x, int y, Color color){
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public double distanceFromMid(int size){
		double mid;
		double dist;

		mid = (size-1)/2.0;
		

		if(abs(mid-x) >= abs(mid-y)){
			dist = abs(mid-x);
		}
		else{
			dist = abs(mid-y);
		}
		return dist;

	}

	public static double abs(double a){
		if (a >= 0) {
			return a;
		}
		else{
			return -a;
		}
	}

}