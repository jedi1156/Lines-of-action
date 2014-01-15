package put.ai.snort.zajpewplayer;

import put.ai.snort.game.Move;

public class MoveWithValue {
	Move move;
  int value;

	public MoveWithValue(int heuristicValue, Move move) {
		this.value = heuristicValue;
		this.move = move;
	}
}
