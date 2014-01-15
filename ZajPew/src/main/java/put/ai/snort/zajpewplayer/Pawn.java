package put.ai.snort.zajpewplayer;

import java.lang.*;

public class Pawn{
	int x;
	int y;

	public Pawn(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double distanceFromMid(int size) {
		double mid;
		double dist;

		mid = (size-1) / 2.0;
		

		if(Math.abs(mid-x) >= Math.abs(mid-y)) {
			dist = Math.abs(mid-x);
		} else{
			dist = Math.abs(mid-y);
		}
		return dist * dist;
	}
}