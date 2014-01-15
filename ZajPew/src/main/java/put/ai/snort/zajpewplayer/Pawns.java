package put.ai.snort.zajpewplayer;

import java.util.ArrayList;
import put.ai.snort.game.Player.Color;
import put.ai.snort.game.Board;

public class Pawns {
  ArrayList<Pawn> pawns;
  int size;

  public Pawns(Board b) {
    pawns = new ArrayList<Pawn>();
    size = b.getSize();
    Color color;
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        color = b.getState(i, j);
        if(color != Color.EMPTY) {
          pawns.add(new Pawn(i, j, color));
        }
      }
    }
  }

  public int value() {
    int result = 0;
    for (Pawn pawn : pawns) {
      result += pawn.value(size);
    }
    return result;
  }
}
