
public class OthelloBoard {
	private Boolean[][] board;
	boolean p1Turn; //where true is black and false is white
	boolean passed, isGameOver;
	String p1AI, p2AI;
	
	public OthelloBoard(int boardSize) {
		board = new Boolean[boardSize][boardSize];
		
		board[boardSize/2-1][boardSize/2-1] = false;
		board[boardSize/2][boardSize/2-1] = true;
		board[boardSize/2-1][boardSize/2] = true;
		board[boardSize/2][boardSize/2] = false;
		
		p1Turn = true;
		passed = isGameOver = false;
		p1AI = "Random";
		p2AI = "Greedy";
	}
	public OthelloBoard(int boardSize, Boolean[][] inputBoard) {
		board = inputBoard.clone();
		
		board[boardSize/2-1][boardSize/2-1] = false;
		board[boardSize/2][boardSize/2-1] = true;
		board[boardSize/2-1][boardSize/2] = true;
		board[boardSize/2][boardSize/2] = false;
		
		p1Turn = true;
		passed = isGameOver = false;
		p1AI = "Random";
		p2AI = "Greedy";
	}
	//Initialize board with starting pieces, where boardSize is never odd
	public Boolean[][] getBoard(){
		return board;
	}
	
	public void resetBoard() {
		board = new Boolean[board.length][board.length];
		
		board[board.length/2-1][board.length/2-1] = false;
		board[board.length/2][board.length/2-1] = true;
		board[board.length/2-1][board.length/2] = true;
		board[board.length/2][board.length/2] = false;
		
		p1Turn = true;
		passed = isGameOver = false;
	}
	
	//Returns a list of ints that are valid coordinates to move to for the given player
	public int[][] getLegalMoves() {
		int out[][] = new int[board.length][board.length];		
		int count;
		int points;
		for(int x = 0; x < board.length; x++)
			for(int y = 0; y < board.length; y++) {
				points = 0;
				if(board[x][y]==null)			
					for(int[] i: getAdjacents(x,y)){
						count = 0;
						int xi = i[0] - x;
						int yi = i[1] - y;
						int xs = i[0];
						int ys = i[1];
						while(true){
							if(xs<board.length&&xs>-1&&ys<board.length&&ys>-1){
								if(board[xs][ys]!=null && board[xs][ys]==p1Turn){
									points+=count;
									break;
								}else if(board[xs][ys]==null){
									break;
								}
								count++;
							}else{
								break;
							}
							xs += xi;
							ys += yi;
						}						
					}
				out[x][y] = points;
			}
		return out;
	}
	
	public int[][] getAdjacents(int x, int y) {
		int[][] adjacents = new int[((x==0||x==board.length-1)?2:3)*((y==0||y == board.length-1)?2:3)-1][2];
		int count = 0;
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(x+i < board.length && x+i >= 0 && y+j < board.length && y+j >= 0 && !(i == 0 && j == 0))
				{
					adjacents[count][0] = x+i;
					adjacents[count][1] = y+j;
					count++;
				}
			}
		}
		return adjacents;
	}
	
	public void takeTurn() {
		int[][] moves = getLegalMoves();
		int movesLeft = 0;
		for(int[] i: moves)
			for(int j: i){
				if(j > 0) movesLeft++;
			}
		if(movesLeft==0) {
			pass();
		}
		else {
			if(p1AI.equals("Greedy")) {
				int maxX = 0, maxY = 0;
				for(int i = 0; i < moves.length; i++) {
					for(int j = 0; j < moves[0].length; j++) {
						if(moves[maxX][maxY] < moves[i][j] || (moves[maxX][maxY] == moves[i][j])&&Math.random()<.2) {
							maxX = i;
							maxY = j;
						}
					}
				}
				move(maxX, maxY, p1Turn);
			}
			else {
				int x = 0, y = 0;
				do{
					x = (int)(Math.random() * 9);
					y = (int)(Math.random() * 9); 
				}while(moves[x][y] > 0);
				move(x, y, p1Turn);
			}
		}		
	}
	
	//Used by takeTurn()
	public void move(int x, int y, boolean turn) {
		board[x][y] = turn;
		passed = false;
		for(int[] i: getAdjacents(x,y)){			
			int xi = i[0] - x;
			int yi = i[1] - y;
			int xs = i[0];
			int ys = i[1];
			int xf,yf;			
			while(true){
				if(xs<board.length&&xs>-1&&ys<board.length&&ys>-1){
					
					if(board[xs][ys]!=null && board[xs][ys]==turn){
						xf = xs;
						yf = ys;
						
						while(true){								
							if(!(xf==x && yf==y)){								
								board[xf][yf] = turn;
							}else{
								break;
							}
							xf -= xi;
							yf -= yi;
						}
						break;
					}else if(board[xs][ys]==null){
						break;
					}
				}else{
					break;
				}
				xs += xi;
				ys += yi;
			}
		}
		p1Turn = !p1Turn;
	}
	
	public int[] getScores() {
		int[] scores = new int[3];
		for(Boolean[] i : board) {
			for(Boolean j : i) {
				if(j != null && j)
					scores[0]++;
				else if(j != null && !j)
					scores[1]++;
				else
					scores[2]++;
			}
		}
		if(scores[0] > scores[1])
			scores[0] += scores[2];
		else if(scores[0] < scores[1])
			scores[1] += scores[2];
			
		
		return scores;
	}
	public void pass() {
		if(!passed)
			passed = true;
		else
			isGameOver = true;		
	}
	
}
