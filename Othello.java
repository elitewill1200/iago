import java.io.IOException;
import java.util.Arrays;

import com.nativelibs4java.opencl.CLBuildException;

public class Othello {
	public static void main(String[] args) throws CLBuildException, IOException {
		int popSize = 100;
		int gamesPer = 100;
		int iterations = 25;
		neuralNet[] population = new neuralNetGPU[popSize];
		int hiddenLayers = 8;
		int neuronsPerLayer = 8;
		JCL gpu = new JCL();
		for(int i = 0; i < popSize; i++) {
			population[i] = new neuralNetGPU(64,hiddenLayers,neuronsPerLayer,64,gpu);
		}
		OthelloBoard board = new OthelloBoard(8);
		int[] winsLossesTies = new int[3];
		for(int iteration = 0; iteration < iterations; iteration++) {
			for(neuralNet net:population) {
				winsLossesTies[0] = winsLossesTies[1] = winsLossesTies[2] = 0;
				for(int i = 0; i < gamesPer; i++) {				
					while(!board.isGameOver) {
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
					board.resetBoard();
				}
				net.fitness = winsLossesTies[1] + winsLossesTies[2] * 0.5;
			}
			Arrays.sort(population);
			//for(int i = 0; i < popSize; i++) {
			//	System.out.println(iteration+", "+population[i].fitness);
			//}
			System.out.println(iteration+" "+population[0].fitness+" "+population[popSize-1].fitness);
			//Selecting parents for the next generation
			neuralNet[] parents = new neuralNet[6*popSize/10];
			for(int parent = 0; parent < parents.length; parent++)
			{
				//Tournament selection breeding of the top 10% of the population
				int k = 5;
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
			{
				population[popSize-1-i/2]=nextGen.xOver(parents[i],parents[i+1]);
			}
			
			for(int i = popSize/10; i < 7*popSize/10; i++)
			{
				nextGen.mutate(population[i], 0.001*(1+i-popSize/10)/6);
			}
		}
	}
}
