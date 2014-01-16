/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.zajpewplayer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import put.ai.snort.game.Board;
import put.ai.snort.game.Move;
import put.ai.snort.game.Player;
import put.ai.snort.game.Player.Color;

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
        
        for (Move move : moves) {
        	b.doMove(move);
        	if(winningBoard(b, getColor())) {
        		b.undoMove(move);
        		return move;
        	}
        	b.undoMove(move);
        }
        
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
    
    private boolean winningBoard(Board b, Color c) {
    	return allConnected(b, c);
    }

    private boolean allConnected(Board b, Color c) {
        int all = countInColor(b, c);
        if (all <= 1) {
            return true;
        }
        for (int x = 0; x < b.getSize(); ++x) {
            for (int y = 0; y < b.getSize(); ++y) {
                if (b.getState(x, y) == c) {
                    return countReachable(b, x, y) == all;
                }
            }
        }
        return true;
    }

	private int countInColor(Board b, Color c) {
	    int n = 0;
	    for (int x = 0; x < b.getSize(); ++x) {
	        for (int y = 0; y < b.getSize(); ++y) {
	            if (b.getState(x, y) == c) {
	                ++n;
	            }
	        }
	    }
	    return n;
	}
	
    private static class Point {

        public int[] p = new int[2];

        public Point(int x, int y) {
            this.p[0] = x;
            this.p[1] = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point)) {
                return false;
            }
            Point other = (Point) obj;
            return p[0] == other.p[0] && p[1] == other.p[1];
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(p);
        }

        public int getX() {
            return p[0];
        }

        public int getY() {
            return p[1];
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", getX(), getY());
        }
    }
    
    private int countReachable(Board b, int x, int y) {
        Set<Point> visited = new HashSet<>();
        Deque<Point> queue = new ArrayDeque<>();
        queue.add(new Point(x, y));
        Color c = b.getState(x, y);
        Point current;
        int n = 0;
        while ((current = queue.pollFirst()) != null) {
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            if (b.getState(current.getX(), current.getY()) == c) {
                ++n;
                findNeighbours(b, current, visited, queue);
            }
        }
        return n;
    }
    
    private void findNeighbours(Board b, Point current, Set<Point> visited, Deque<Point> queue) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                Point p = new Point(current.getX() + dx, current.getY() + dy);
                if (!isValid(b, p.getX(), p.getY())) {
                    continue;
                }
                if (!visited.contains(p)) {
                    queue.add(p);
                }
            }
        }
    }

    private boolean isValid(Board b, int x, int y) {
        return !(x < 0 || y < 0 || x >= b.getSize() || y >= b.getSize());
    }
}
