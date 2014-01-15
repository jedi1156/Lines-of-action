package put.ai.snort.zajpewplayer;

import java.util.Comparator;

import put.ai.snort.game.Move;

public class MovesComparator implements Comparator<MoveWithValue> {

	@Override
	public int compare(MoveWithValue o1, MoveWithValue o2) {
		return o1.value - o2.value;
	}

}
