import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerVNet {
	public static void main(String[] args){
	neuralNet net = new neuralNet(0,0,0,0);
	neuralNet pnet = new playerNet();
	Scanner sc = new Scanner(System.in);


	System.out.print("enter options :");
	//A 1 1 0
	//B 1 1 149
	//C 1 1 999
	//D 8 8 0
	//E 8 8 149
	//F 8 8 999
	int selection = sc.nextLine().toUpperCase().charAt(0);
	int hiddenLayers = 1;
	int neuronsPerLayer = 1;
	int gens = 149;
	switch (selection) {
	case 65:
		hiddenLayers = 1;
		neuronsPerLayer = 1;
		gens = 0;
		break;
	case 66:
		hiddenLayers = 1;
		neuronsPerLayer = 1;
		gens = 149;
		break;
	case 67:
		hiddenLayers = 1;
		neuronsPerLayer = 1;
		gens = 999;
		break;
	case 68:
		hiddenLayers = 8;
		neuronsPerLayer = 8;
		gens = 0;
		break;
	case 69:
		hiddenLayers = 8;
		neuronsPerLayer = 8;
		gens = 149;
		break;
	case 70:
		hiddenLayers = 8;
		neuronsPerLayer = 8;
		gens = 999;
		break;
	}
	
	try (ObjectInputStream ois =
			new ObjectInputStream(new FileInputStream(String.format(".\\src\\nets\\L%dNPL%dNGen%d.net",hiddenLayers,neuronsPerLayer,gens)))) {

		net = ((neuralNet[]) ois.readObject())[0];
	}catch (FileNotFoundException ex1){
	}catch (Exception ex) {
		ex.printStackTrace();
	}
	//OthelloBoard board = new OthelloBoard(8);
	AtomicInteger wins = new AtomicInteger(0);
	AtomicInteger losses = new AtomicInteger(0);
	AtomicInteger ties = new AtomicInteger(0);
	wins.set(0);
	losses.set(0);
	ties.set(0);
	new Gameplay(net, pnet, Math.random()<.5, wins, losses, ties).run();
	
	}
}