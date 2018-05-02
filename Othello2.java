import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Othello2 {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
	public static Boolean[][] createProblemBoard(int size, String blackCoords, String whiteCoords) {
		Boolean[][] board = new Boolean[size][size];
		
		for(int i = 0; i < blackCoords.length() - 1; i += 2)
			board[Integer.parseInt(blackCoords.charAt(i)+"")][Integer.parseInt(blackCoords.charAt(i+1)+"")] = true;
		
		for(int i = 0; i < whiteCoords.length() - 1; i += 2)
			board[Integer.parseInt(whiteCoords.charAt(i)+"")][Integer.parseInt(whiteCoords.charAt(i+1)+"")] = false;
		
		return board;
	}
	public static void main(String[] args){			
		ArrayList<Boolean[][]> boards = new ArrayList<Boolean[][]>();
		
		//boards.add(createProblemBoard(3, "00", "01"));
		//boards.add(createProblemBoard(3, "000110", "11"));
		//boards.add(createProblemBoard(4, "0010", "1112"));
		//boards.add(createProblemBoard(4, "0110", "1112"));
		//boards.add(createProblemBoard(5, "000134", "1133"));
		//boards.add(createProblemBoard(4, "000110", "1112"));
		//boards.add(createProblemBoard(5, "00010220", "1123"));
		//boards.add(createProblemBoard(4, "00033033", "112131"));
		//boards.add(createProblemBoard(5, "21", "2224"));
		boards.add(createProblemBoard(4, "2123", "1220"));
		boards.add(createProblemBoard(4, "1222", "1121"));
		//boards.add(createProblemBoard(5, "0024", "1121"));
		//boards.add(createProblemBoard(5, "002430", "1121"));
		//boards.add(createProblemBoard(4, "0022313233", "0110111221"));
		//boards.add(createProblemBoard(4, "010211313233", "1013212223"));
		//boards.add(createProblemBoard(6, "1112212233344344", "1314232431324142"));
		//boards.add(createProblemBoard(4, "1222", "1121"));
		//boards.add(createProblemBoard(4, "1122", "1221"));
		
		for(int initState = 0; initState < boards.size(); initState++) {
			int[][] generation = new int[8][8];
			for(int x =0; x < 8; x++)
				for(int y =0; y<8; y++)
					generation[x][y] = -1;
			for(int neuronsPerLayer = 1; neuronsPerLayer <= 8;neuronsPerLayer++) 
				for(int hiddenLayers = 1; hiddenLayers <= 8;hiddenLayers++) {
					String date = sdf.format(new Timestamp(System.currentTimeMillis()));
					boolean saveState = true;
					boolean continueState = false;
					int popSize = 100;
					int gamesPer = 250;
					int totalGens = 1000;//###########################################################################################
					
					
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
							population[i] = new neuralNet((int)Math.pow(boards.get(initState).length,2),hiddenLayers,neuronsPerLayer,(int)Math.pow(boards.get(initState).length,2));
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
							if(generation[neuronsPerLayer-1][hiddenLayers-1] == -1) {
								wins.set(0);
								losses.set(0);
								ties.set(0);
								for(int i = 0; i < gamesPer; i++) {
									new Thread(new Gameplay(net, false, wins, losses, ties,boards.get(initState))).start();							
								}
								netIndex++;
								while(wins.get() + ties.get() + losses.get() < gamesPer) {					
								}
								net.fitness = (wins.get() + ties.get() * 0.5)/gamesPer * 100;
								
								if (net.fitness == 100) {
									generation[neuronsPerLayer-1][hiddenLayers-1] = gen;
									break;
								}
							}
						}
						Arrays.sort(population);	
						System.out.println(gen+" "+hiddenLayers+" "+neuronsPerLayer+" "+population[0].fitness);
						if(generation[neuronsPerLayer-1][hiddenLayers-1]!=-1) {
							break;
						}
																		
						
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
			
			try (FileOutputStream fos =	new FileOutputStream(String.format(".\\src\\csvs\\P%d.csv",initState),true)) {
				String out = "neuronsPerLayer,hiddenLayers,problem,gen\n";;
				for(int neuronsPerLayer = 1; neuronsPerLayer <= 8;neuronsPerLayer++) 					
					for(int hiddenLayers = 1; hiddenLayers <= 8;hiddenLayers++) {
						
						out += neuronsPerLayer + "," + hiddenLayers + "," +generation[neuronsPerLayer-1][hiddenLayers-1] +"\n";
					}
				fos.write(out.getBytes());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
}