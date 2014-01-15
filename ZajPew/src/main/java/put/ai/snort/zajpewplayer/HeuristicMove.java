package put.ai.snort.zajpewplayer;

import put.ai.snort.game.Move;

public class HeuristicMove{
	int heuristic;
	Move move;
	public HeuristicMove(int heuristic, Move move) {
		this.heuristic=heuristic;
		this.move=move;
	}
}
