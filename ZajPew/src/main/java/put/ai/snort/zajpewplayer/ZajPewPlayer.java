/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.zajpewplayer;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

import put.ai.snort.game.Board;
import put.ai.snort.game.Move;
import put.ai.snort.game.Player;

public class ZajPewPlayer extends Player {
	
	public static void main(String []args) {
	}

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
        	alfa = alphaBeta(b,depth - 1, alfa, beta, opColor);
        	if (max<alfa) {
        		max = alfa;
        		best = move;
        	}

        	b.undoMove(move);
        }
        return best;
    }

    public int alphaBeta(Board b, int depth, int alpha, int beta, Color color){
    	List<Move> moves = b.getMovesFor(color);
        if (moves.size() == 0) {
        	if (color == getColor())
        		return -boardSize;
        	else
        		return boardSize;
        }
        else if (depth == 0) {
            return heuristic(b, getOpponent(color));
        }
        else if (color == getColor()) {
            for (Move move: moves) {
                b.doMove(move);
                alpha = Math.max(alpha, alphaBeta(b, depth - 1, alpha, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alpha){
                    break;
                }
            }
            return alpha;
        }
        else {
            for (Move move: moves) {
                b.doMove(move);
                beta = Math.min(beta, alphaBeta(b, depth - 1, alpha, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alpha){
                    break;
                }
            }
            return beta;
        }
    }


    public int heuristic (Board b, Color color) {
        ArrayList<Pawn> pawnsOfFirstPlayer = getPawnsForBoard(b, Color.PLAYER1);
        ArrayList<Pawn> pawnsOfSecondPlayer = getPawnsForBoard(b, Color.PLAYER2);
        	
        int boardSize = b.getSize();
        double value = valueOfPawns(pawnsOfSecondPlayer, boardSize) - valueOfPawns(pawnsOfFirstPlayer, boardSize);
        
        if (color == Color.PLAYER1) {
            return - (int)value;
        }
        else {
        	return (int)value;
        }
    }

    public ArrayList<Pawn> getPawnsForBoard(Board b, Color color) {
        ArrayList<Pawn> result = new ArrayList<Pawn>();
        int size = b.getSize();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if(color == b.getState(i, j)) {
                    result.add(new Pawn(i, j, color));
                }
            }
        }
        return result;
    }

    public double valueOfPawns(ArrayList<Pawn> pawns, int size) {
        double result = 0.0;
        for(Pawn pawn : pawns) {
        	result += pawn.distanceFromMid(size);
        }
        return result;
    }
}
