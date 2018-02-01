import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Othello {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");

	public static void main(String[] args){		
		for(int neuronsPerLayer = 1; neuronsPerLayer <= 8;neuronsPerLayer++) 
			for(int hiddenLayers = 1; hiddenLayers <= 8;hiddenLayers++) {
				String date = sdf.format(new Timestamp(System.currentTimeMillis()));
				boolean saveState = true;
				boolean continueState = false;
				int popSize = 250;
				int gamesPer = 250;
				int totalGens = 150;//###########################################################################################
				int mode = 1; //where 0 = net v greedy AI, 1 = net v net, 2 = both
				neuralNet[] population = new neuralNet[popSize];
				neuralNet[] oldPopulation =null;
				float[] data = new float[popSize/10];

				if(continueState)
					try (ObjectInputStream ois =
					new ObjectInputStream(new FileInputStream(String.format(".\\src\\nets\\L%dNPL%dN.net",hiddenLayers,neuronsPerLayer)))) {

						oldPopulation = (neuralNet[]) ois.readObject();
					}catch (FileNotFoundException ex){
					}catch (Exception ex) {
						ex.printStackTrace();
					}

				for(int i = 0; i < popSize; i++) {
					if(oldPopulation!=null&&i<oldPopulation.length){
						population[i] = oldPopulation[i];
					}else{
						population[i] = new neuralNet(64,hiddenLayers,neuronsPerLayer,64);
					}
				}
				//OthelloBoard board = new OthelloBoard(8);
				AtomicInteger wins = new AtomicInteger(0);
				AtomicInteger losses = new AtomicInteger(0);
				AtomicInteger ties = new AtomicInteger(0);
				int netIndex;
				for(int gen = 0; gen < totalGens; gen++) {					
					netIndex = 0;
					//net2Index = popSize - 1;
					for(neuralNet net:population) {
						wins.set(0);
						losses.set(0);
						ties.set(0);
						for(int i = 0; i < gamesPer; i++) {
							switch(mode) {
							case 2:
								break;
							case 1:
								int opponentIndex;
								do {
									opponentIndex = (int)(Math.random() * popSize);
								} while(opponentIndex == netIndex);						
								new Thread(new Gameplay(net, population[opponentIndex], i<gamesPer/2? true:false, wins, losses, ties)).start();						
								break;
							case 0:
							default:
								new Thread(new Gameplay(net, i<gamesPer/2? true:false, wins, losses, ties)).start();
							}

							//Gameplay gameplay = new Gameplay(net, i<gamesPer/2? true:false);
							//Thread t = new Thread(gameplay);

							/*if(netIndex < popSize / 2) {
						gameplays[i] = new Gameplay(net, i<gamesPer/2? true:false, wins, losses, ties);
					}
					else if(netIndex < popSize*3/4) {
						gameplays[i] = new Gameplay(net, population[net2Index], i<gamesPer/2? true:false, wins, losses, ties);
						net2Index--;
					}
					threads[i] = new Thread(gameplays[i]);
					threads[i].start();
					netIndex++;*/


							/*while(!board.isGameOver) {
						if(i<gamesPer/2) {
							board.takeTurn();
							net.takeTurn(board, false);		
						}else {							
							net.takeTurn(board, true);
							board.takeTurn();
						}
					}
					int[] scores = board.getScores();
					if((scores[0] > scores[1]&&i<gamesPer/2)||(scores[0] < scores[1]&&i>gamesPer/2))
						winsLossesTies[0]++;
					else if((scores[0] < scores[1]&&i<gamesPer/2)||(scores[0] > scores[1]&&i>gamesPer/2))
						winsLossesTies[1]++;
					else
						winsLossesTies[2]++;
					board.resetBoard();*/
						}
						netIndex++;
						while(wins.get() + ties.get() + losses.get() < gamesPer) {					
						}
						net.fitness = (wins.get() + ties.get() * 0.5)/gamesPer * 100;
					}
					Arrays.sort(population);
					for(int i = 0; i < popSize/10; i++)
					{
						wins.set(0);
						losses.set(0);
						ties.set(0);
						for(int j = 0; j < 100; j++)
						{
							new Thread(new Gameplay(population[i], i<j/2? true:false, wins, losses, ties)).start();
						}
						while(wins.get() + ties.get() + losses.get() < 100) {					
						}
						data[i]=wins.get()+ties.get()/2;
					}
					try (FileOutputStream fos =	new FileOutputStream(String.format(".\\src\\csvs\\L%dNPL%dT%s.csv",hiddenLayers,neuronsPerLayer,date),true)) {
						String out = "";
						for(int i = 0; i < popSize/10; i++) {
							out += gen + "," + data[i] + "\n";
						}

						fos.write(out.getBytes());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					//for(int i = 0; i < popSize; i++) {
					//	System.out.println(gen+", "+population[i].fitness);
					//}
					System.out.println(gen+" "+hiddenLayers+" "+neuronsPerLayer);
					//Selecting parents for the next generation
					neuralNet[] parents = new neuralNet[6*popSize/10];
					for(int parent = 0; parent < parents.length; parent++)
					{
						//Tournament selection breeding of the top 10% of the population
						int k = (int)(popSize*0.05);
						for(int i = 0; i < popSize*0.6; i++)
						{
							neuralNet[] tournament = new neuralNet[k];
							neuralNet[] populationCopy = population.clone();
							for(int l = 0; l < k; l++)
							{
								int contestant;
								do {
									contestant = (int)(Math.random()*popSize*0.1);
									tournament[l]=populationCopy[contestant];
								} while(tournament[l] == null);
								populationCopy[l] = null;
							}
							double p = 0.75;

							Arrays.sort(tournament);
							parents[parent] = tournament[0];
							double rng = Math.random();
							for(int j = k; j > 0; j--) {
								if(p*Math.pow(1-p, j) - rng >= 0) {
									parents[parent] = tournament[j-1];
								}
								rng -= p*Math.pow(1-p, j);
							}
							/*double[] probabilities = new double[k];
					for(int j = 0; j < k; j++)
						probabilities[j]=p*Math.pow(1-p,j);
					double seed = Math.random();
					int choice = 0;
					while(seed>probabilities[choice])
					{
						seed-=probabilities[choice];
						choice+=1;
					}

					parents[parent] = tournament[choice];*/
						}
					}
					for(int i = 0; i < parents.length; i+=2)
						population[popSize-1-i/2]=nextGen.xOver(parents[i],parents[i+1]);

					for(int i = popSize/10; i < 7*popSize/10; i++)
						nextGen.mutate(population[i], 0.1 * (i-popSize/10)/(6*popSize));
				}

				if(saveState){
					try (ObjectOutputStream oos =
							new ObjectOutputStream(
									new FileOutputStream(String.format(".\\src\\nets\\L%dNPL%dT%s.net",hiddenLayers,neuronsPerLayer,date)))) {

						oos.writeObject(population);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					try (ObjectOutputStream oos =
							new ObjectOutputStream(
									new FileOutputStream(String.format(".\\src\\nets\\L%dNPL%dN.net",hiddenLayers,neuronsPerLayer)))) {

						oos.writeObject(population);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}		
	}
}
