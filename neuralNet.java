public class neuralNet implements Comparable<neuralNet>{
	double[][] inputLayer;
	double[][][] hiddenNet;
	double[][] outputLayer;
	int inputs,hiddenLayers,neuronsPerLayer,outputs;
	double fitness;
	public neuralNet(int inputs, int hiddenLayers, int neuronsPerLayer, int outputs){
		this.inputs = inputs;
		this.hiddenLayers = hiddenLayers;
		this.neuronsPerLayer = neuronsPerLayer;
		this.outputs = outputs;
		fitness = 0.0;
		inputLayer = new double[neuronsPerLayer][inputs+1];
		for(int x = 0; x < neuronsPerLayer; x++) {
			for(int y = 0; y <= inputs; y++) {
				inputLayer[x][y] = -10 + (Math.random() * 20);
			}
		}
		if(hiddenLayers>1){
			hiddenNet = new double[hiddenLayers-1][neuronsPerLayer][neuronsPerLayer+1];
			for(int x = 0; x < hiddenLayers-1; x++) {
				for(int y = 0; y < neuronsPerLayer; y++) {
					for(int z = 0; z <= neuronsPerLayer; z++) {
						hiddenNet[x][y][z] = -10 + (Math.random() * 20);
					}
				}
			}
		}
		outputLayer = new double[outputs][neuronsPerLayer+1];
		for(int x = 0; x < outputs; x++) {
			for(int y = 0; y <= neuronsPerLayer; y++) {
				outputLayer[x][y] = -10 + (Math.random() * 20);
			}
		}
	}
	
	public void takeTurn(OthelloBoard o, boolean minMax){
		Boolean[][] values = o.getBoard();
		int[][] legalMoves = o.getLegalMoves();
		double[] in = new double[neuronsPerLayer];
		double[] out = new double[neuronsPerLayer];
		double[] finals = new double[outputs];
		int minMaxIndex = 0;
		for(int x = 0; x < neuronsPerLayer; x++) {
			for(int y = 0; y < inputs; y++) {
				in[x] += inputLayer[x][y] * (values[y%8][y/8]==null?0:(values[y%8][y/8]?1:-1));
			}
			in[x] += inputLayer[x][inputs];
		}

		for(int x = 0; x < hiddenLayers-1; x++) {
			for(int y = 0; y < neuronsPerLayer; y++) {
				for(int z = 0; z < neuronsPerLayer; z++) {
					out[y] += hiddenNet[x][y][z] * in[z];
				}
				out[y] += hiddenNet[x][y][neuronsPerLayer];
			}
			for(int i = 0; i < neuronsPerLayer; i++) {
				in[i] = out[i];
			}
		}

		for(int x = 0; x < outputs; x++) {
			for(int y = 0; y < neuronsPerLayer; y++) {
				finals[x] += outputLayer[x][y] * in[y];
			}
			finals[x] += outputLayer[x][neuronsPerLayer];
		}
		
		if(minMax) {
			for(int i = 1; i < outputs; i++) {
				if(legalMoves[i%8][i/8]!=0&&(finals[minMaxIndex]<finals[i]||legalMoves[minMaxIndex%8][minMaxIndex/8]==0)) {
					minMaxIndex = i;
				}
			}			
		}else {
			for(int i = 1; i < outputs; i++) {	
				
				if(legalMoves[i%8][i/8]!=0&&(finals[minMaxIndex]>finals[i]|legalMoves[minMaxIndex%8][minMaxIndex/8]==0)) {
					minMaxIndex = i;
				}
			}	
		}
		if(legalMoves[minMaxIndex%8][minMaxIndex/8]==0) {
			o.pass();
		}else {
			o.move(minMaxIndex%8, minMaxIndex/8, minMax);
		}
	}

	@Override
	public int compareTo(neuralNet compareNet) {
		
		if(this.fitness<compareNet.fitness)
	          return 1;
	    else if(compareNet.fitness<this.fitness)
	          return -1;
	    return 0;
	}
}
