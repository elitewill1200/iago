
public class nextGen {
	public static neuralNet xOver(neuralNet p1,neuralNet p2) {
		neuralNet c = new neuralNet(p1.inputs,p1.hiddenLayers,p1.neuronsPerLayer,p1.outputs);
		for(int x = 0; x < p1.neuronsPerLayer; x++)
			for(int y = 0; y <= p1.inputs; y++)
				c.inputLayer[x][y] = Math.random() > 0.5?p1.inputLayer[x][y]:p2.inputLayer[x][y];
				
		for(int x = 0; x < p1.hiddenLayers; x++)
			for(int y = 0; y < p1. neuronsPerLayer; y++)
				for(int z = 0; z <= p1.neuronsPerLayer; z++)
					c.hiddenNet[x][y][z] = Math.random() > 0.5?p1.hiddenNet[x][y][z]:p2.hiddenNet[x][y][z];
					
		for(int x = 0; x < p1.outputs; x++)
			for(int y = 0; y <= p1.neuronsPerLayer; y++)
				c.outputLayer[x][y] = Math.random() > 0.5?p1.outputLayer[x][y]:p2.outputLayer[x][y];
		return c;
	}
	public static void mutate(neuralNet p1,double factor) {
		for(int x = 0; x < p1.neuronsPerLayer; x++)
			for(int y = 0; y <= p1.inputs; y++)
				p1.inputLayer[x][y] = (float) (Math.random() > factor?p1.inputLayer[x][y]:(-10 + (Math.random() * 20)));
				
		for(int x = 0; x < p1.hiddenLayers; x++)
			for(int y = 0; y < p1. neuronsPerLayer; y++)
				for(int z = 0; z <= p1.neuronsPerLayer; z++)
					p1.hiddenNet[x][y][z] = (float) (Math.random() > factor?p1.hiddenNet[x][y][z]:(-10 + (Math.random() * 20)));
					
		for(int x = 0; x < p1.outputs; x++)
			for(int y = 0; y <= p1.neuronsPerLayer; y++)
				p1.outputLayer[x][y] = (float) (Math.random() > factor?p1.outputLayer[x][y]:(-10 + (Math.random() * 20)));
					
	}
	
}
