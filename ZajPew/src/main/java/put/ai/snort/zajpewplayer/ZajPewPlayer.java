/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.zajpewplayer;

import java.util.List;
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
        List<Move> moves = b.getMovesFor(getColor());
        
    	PriorityQueue<MoveWithValue> movesQueue = new PriorityQueue<MoveWithValue>(moves.size(), new MovesComparator());
        for (Move move : moves) {
        	b.doMove(move);
        	movesQueue.add(new MoveWithValue(-heuristic(b,getColor()), move));
        	b.undoMove(move);
        }

        Move bestMove = null;
        Color opColor = getOpponent(getColor());
    	boardSize = (int)Math.pow(b.getSize(), 2);
        int max = - (boardSize - 1);
    	int depth = 4;
        int beta = boardSize;
        int alpha = - boardSize;
        while(!movesQueue.isEmpty()) {
        	Move move = movesQueue.poll().move;
        	b.doMove(move);
        	alpha = alphaBeta(b,depth - 1, alpha, beta, opColor);
        	if (max<alpha) {
        		max = alpha;
        		bestMove = move;
        	}

        	b.undoMove(move);
        }
        return bestMove;
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
        int value = (new Pawns(b)).value();

        if (color == Color.PLAYER2) {
            return -value;
        }
        return value;
    }

}
