import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
public class theRightWay {
	boolean printSteps = false;
	public static void main(String[] args) {
		Queue<OthelloBoard> search = new LinkedList<>();
		Queue<ArrayList<int[]>> madeMoves = new LinkedList<>();
		ArrayList<OthelloBoard> finishedBoards = new ArrayList<OthelloBoard>();
		ArrayList<ArrayList<int[]>> finishedMoves = new ArrayList<ArrayList<int[]>>();
		
		Boolean[][] input = new Boolean[4][4];
		
		int boardSize = 4;
		input[0][1]=true;
		input[1][1]=true;
		input[2][1]=true;
		input[1][2]=true;
		input[2][2]=false;
		
		int maxMoves = 999;
		
		OthelloBoard b,e; 
		int[][] moves;
		ArrayList<int[]> pastMoves;
		boolean hasChildren;
		search.add(new OthelloBoard(boardSize));
		madeMoves.add(new ArrayList<int[]>());
		while(!search.isEmpty()) {
			b = search.poll();
			pastMoves = madeMoves.poll();
			hasChildren = false;
			
			if(b.getGameOver() == false && b.getMove() < maxMoves) {
				moves = b.getLegalMoves();				
				for(int x = 0;x < moves.length;x++ ) 
					for(int y = 0; y < moves[x].length; y++) 
						if(moves[x][y] > 0) {
							e = new OthelloBoard(boardSize,b.getBoard(),b.getInitial(),b.getMove(),b.getp1Turn(),b.getPassed());
							e.move(x,y,e.getp1Turn());
							ArrayList<int[]> m = new ArrayList<int[]>(pastMoves);
							m.add(new int[] {x,y});
							search.add(e);
							madeMoves.add(m);							
							hasChildren = true;
						}
				if (!hasChildren) {
					b.pass();
					search.add(b);
					pastMoves.add(new int[] {-1,-1});
					madeMoves.add(pastMoves);
				}
				
			} else {
				finishedBoards.add(b);
				finishedMoves.add(pastMoves);
				//b.printBoard();
			}			
		}
		int k = 0;
		int l = maxMoves;
		for(OthelloBoard i:finishedBoards) {
			if(i.getMove() > k)
				k = i.getMove();
			if (i.getMove() < l)
				l = i.getMove();
		}
		
		
		System.out.println("There are " + finishedBoards.size()+" ends with a max moves of " + k +" and a min of " + l +" (including passes)");
		int[] move;
		for(int i = 0;i < finishedBoards.size();i++) {
			//System.out.println("New branch");
			b = finishedBoards.get(i);
			b.resetBoard();
			//b.printBoard();
			for(int j = 0; j < finishedMoves.get(i).size(); j++) {
				move = finishedMoves.get(i).get(j);				
				if (move[0] != -1) {
					if (b.getLegalMoves()[move[0]][move[1]]==0)
						System.out.println("Something is wrong");
					b.move(move[0], move[1], b.getp1Turn());
					
				}else {
					//System.out.println("Pass");
					b.pass();
				}
				//b.printBoard();
			}			
		}
	}

}
