import java.util.concurrent.atomic.AtomicInteger;

public class Gameplay implements Runnable {
	public OthelloBoard board;
	public neuralNet net1, net2;
	public boolean minMax;
	public AtomicInteger wins, losses, ties;
	
	public Gameplay(neuralNet net1, boolean minMax, AtomicInteger wins, AtomicInteger losses, AtomicInteger ties, Boolean[][] seedBoard) {
		board = new OthelloBoard(8, seedBoard);
		this.net1 = net1;
		this.minMax = minMax;
		this.wins = wins;
		this.losses = losses;
		this.ties = ties;
	}
	
	public Gameplay(neuralNet net1, neuralNet net2, boolean minMax, AtomicInteger wins, AtomicInteger losses, AtomicInteger ties) {
		board = new OthelloBoard(8);
		this.net1 = net1;
		this.net2 = net2;
		this.minMax = minMax;
		this.wins = wins;
		this.losses = losses;
		this.ties = ties;
	}
	
	@Override
	public void run() {
		while(!board.isGameOver) {
			if(net2 == null) {
				if(minMax) {
					board.takeTurn();
					net1.takeTurn(board, false);
				}
				else {
					net1.takeTurn(board, true);
					board.takeTurn();
				}
			}
			else {
				if(minMax) {
					net2.takeTurn(board, true);
					net1.takeTurn(board, false);
				}
				else {
					net1.takeTurn(board, true);
					net2.takeTurn(board, false);
				}
			}
		}
		int[] scores = board.getScores();
		if(minMax) {
			if(scores[0] > scores[1])
				losses.incrementAndGet();
			else if(scores[0] < scores[1])
				wins.incrementAndGet();
			else
				ties.incrementAndGet();
		}
		else {
			if(scores[0] < scores[1])
				losses.incrementAndGet();
			else if(scores[0] > scores[1])
				wins.incrementAndGet();
			else
				ties.incrementAndGet();
		}
	}
}
