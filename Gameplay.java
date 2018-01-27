import java.util.concurrent.atomic.AtomicInteger;

public class Gameplay implements Runnable {
	public OthelloBoard board;
	public neuralNet net;
	public boolean minMax;
	AtomicInteger wins,losses,ties;
	
	public Gameplay(neuralNet net, boolean minMax, AtomicInteger wins,AtomicInteger losses,AtomicInteger ties) {
		board = new OthelloBoard(8);
		this.net = net;
		this.minMax = minMax;
		this.wins = wins;
		this.losses = losses;
		this.ties = ties;
	}
	
	@Override
	public void run() {
		while(!board.isGameOver) {
			if(minMax) {
				board.takeTurn();
				net.takeTurn(board, false);
			}else {
				net.takeTurn(board, true);
				board.takeTurn();
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
