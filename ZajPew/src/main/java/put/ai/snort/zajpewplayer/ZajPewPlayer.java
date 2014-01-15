/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.zajpewplayer;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import put.ai.snort.game.Board;
import put.ai.snort.game.Move;
import put.ai.snort.game.Player;

public class ZajPewPlayer extends Player {
	
	public static void main(String []args) {
	}

    private Random random=new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Zajączkowski & Pewiński Team";
    }

    int boardSize;
    @Override
    public Move nextMove(Board b) {
    	int depth = 4;
        List<Move> moves = b.getMovesFor(getColor());
    	boardSize = b.getSize()*b.getSize();
        int max = - boardSize-1;
        Move best = null;
        
        int alfa = - boardSize;
        int beta = boardSize;
    	PriorityQueue<MoveWithValue> movesQueue = new PriorityQueue<MoveWithValue>(moves.size(), new MovesComparator());
        for (Move move : moves) {
        	b.doMove(move);
        	movesQueue.add(new MoveWithValue(-heuristic(b,getColor()), move));
        	b.undoMove(move);
        }
        Move move;
        Color opColor = getOpponent(getColor());
        while(!movesQueue.isEmpty()) {
        	move = movesQueue.poll().move;
        	b.doMove(move);
        	alfa = alphabeta(b,depth - 1, alfa, beta, opColor);
        	if (max<alfa) {
        		max = alfa;
        		best = move;
        	}

        	b.undoMove(move);
        }
        return best;
    }

    public int alphabeta(Board b, int depth, int alfa, int beta, Color color){
        if (b.getMovesFor(color).size() == 0) {
        	if (color == getColor())
        		return -boardSize;
        	else
        		return boardSize;
        }
        else if (depth == 0) {
            return heuristic(b, getOpponent(color));
        }
        else if (color == getColor()) {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                alfa = Math.max(alfa, alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                    break;
                }
            }
            return alfa;
        }
        else {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                beta = Math.min(beta, alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                    break;
                }
            }
            return beta;
        }
    }


    public int heuristic (Board b, Color color) {
        ArrayList<Pawn> pawns = getPawnsForBoard(b);

        double value = valueOfPawns(pawns, b.getSize());

        if (color == Color.PLAYER1) {
            return (int) -value;
        }

        return (int)value;

    }

    public ArrayList<Pawn> getPawnsForBoard (Board b) {
        ArrayList<Pawn> result = new ArrayList<Pawn>();
        int size = b.getSize();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Color color = b.getState(i, j);
                if(color != Color.EMPTY) {
                    result.add(new Pawn(i, j, color));
                }
            }
        }
        return result;
    }

    public double valueOfPawns(ArrayList<Pawn> pawns, int size) {
        double res = 0.0;
        for(Pawn p : pawns) {
            if(p.color == Color.PLAYER1) {
                res -= p.distanceFromMid(size);
            } else {
                res += p.distanceFromMid(size);
            }
        }
        return res;
    }
}
