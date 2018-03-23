import java.util.Scanner;

public class playerNet extends neuralNet {

	public playerNet() {
		super(64, 0, 0, 64);
	}

	public void takeTurn(OthelloBoard o, boolean turn){
		Scanner sc = new Scanner(System.in);
		boolean valid=true;
		boolean mustPass=true;
		String move;
		do{
			if(!valid)
				System.out.println("INVALID MOVE, PLEASE TRY AGAIN.");
			System.out.print("YOUR MOVE: ");
			move = sc.nextLine().toUpperCase();
			valid = (move.length()==2&&move.charAt(0)<73&&move.charAt(0)>64&&move.charAt(1)<57&&move.charAt(1)>48);
			for(int i = 0; i < 64; i++)
				mustPass=mustPass&&(o.getLegalMoves()[i%8][i/8]==0);
			valid = valid && o.getLegalMoves()[move.charAt(0)-65][move.charAt(1)-49]!=0;
			valid = valid ||(move.equals("PASS")&&mustPass);
		}while(!valid);
		if(move.equals("PASS"))
			o.pass();
		else
			o.move(move.charAt(0)-65, move.charAt(1)-49, turn);
		
	}
}
