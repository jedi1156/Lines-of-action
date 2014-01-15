package put.ai.snort.zajpewplayer;

import java.lang.*;
import put.ai.snort.game.Player.Color;

public class Pawn {
	int x;
	int y;
  Color color;

	public Pawn(int x, int y, Color color) {
		this.x = x;
		this.y = y;
    this.color = color;
  }

	public int value(int size) {
		int result = Math.min(sideValue(x, size), sideValue(y, size));
		return colorValue() * result * result;
	}

	private int sideValue(int arg, int size) {
		return (size - Math.abs((arg << 1) + 1 - size) - 1) >> 1;
	}

	private int colorValue() {
    if(color == Color.PLAYER1) {
	   	return -1;
    } else if(color == Color.PLAYER2) {
	    return 1;
  	} else {
  		return 0;
  	}
	}

  public String toString() {
    return "(" + x + ", " + y + "): " + colorValue();
  }
}
