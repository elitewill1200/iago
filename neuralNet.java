import java.io.Serializable;

@SuppressWarnings("serial")
public class neuralNet implements Comparable<neuralNet>, Serializable{
	float[][] inputLayer;
	float[][][] hiddenNet;
	float[][] outputLayer;
	int inputs,hiddenLayers,neuronsPerLayer,outputs;
	double fitness;
	public neuralNet(int inputs, int hiddenLayers, int neuronsPerLayer, int outputs){
		this.inputs = inputs;
		this.hiddenLayers = hiddenLayers;
		this.neuronsPerLayer = neuronsPerLayer;
		this.outputs = outputs;
		fitness = 0.0;
		inputLayer = new float[neuronsPerLayer][inputs+1];
		for(int x = 0; x < neuronsPerLayer; x++) {
			for(int y = 0; y <= inputs; y++) {
				inputLayer[x][y] = (float) (-10 + (Math.random() * 20));
			}
		}

		if(hiddenLayers>1){
			hiddenNet = new float[hiddenLayers-1][neuronsPerLayer][neuronsPerLayer+1];
			for(int x = 0; x < hiddenLayers-1; x++) {
				for(int y = 0; y < neuronsPerLayer; y++) {
					for(int z = 0; z <= neuronsPerLayer; z++) {
						hiddenNet[x][y][z] = (float) (-10 + (Math.random() * 20));
					}
				}
			}
		}

		outputLayer = new float[outputs][neuronsPerLayer+1];
		for(int x = 0; x < outputs; x++) {
			for(int y = 0; y <= neuronsPerLayer; y++) {
				outputLayer[x][y] = (float) (-10 + (Math.random() * 20));
			}
		}
	}

	public void takeTurn(OthelloBoard o, boolean minMax){
		Boolean[][] values = o.getBoard();
		int[][] legalMoves = o.getLegalMoves();
		float[] in = new float[neuronsPerLayer];
		float[] out = new float[neuronsPerLayer];
		float[] finals = new float[outputs];
		int minMaxIndex = 0;
		for(int x = 0; x < neuronsPerLayer; x++) {
			for(int y = 0; y < inputs; y++) {
				in[x] += inputLayer[x][y] * (values[y%(int)Math.pow(inputs,0.5)][y/(int)Math.pow(inputs,0.5)]==null?0:(values[y%(int)Math.pow(inputs,0.5)][y/(int)Math.pow(inputs,0.5)]?1:-1));
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
			if(legalMoves[x%(int)Math.pow(inputs,0.5)][x/(int)Math.pow(inputs,0.5)]!=0) {
				for(int y = 0; y < neuronsPerLayer; y++) {
					finals[x] += outputLayer[x][y] * in[y];
				}
				finals[x] += outputLayer[x][neuronsPerLayer];
			}
		}

		if(minMax) {
			for(int i = 1; i < outputs; i++) {
				if(legalMoves[i%(int)Math.pow(inputs,0.5)][i/(int)Math.pow(inputs,0.5)]!=0&&(finals[minMaxIndex]<finals[i]||legalMoves[minMaxIndex%(int)Math.pow(inputs,0.5)][minMaxIndex/(int)Math.pow(inputs,0.5)]==0)) {
					minMaxIndex = i;
				}
			}			
		}else {
			for(int i = 1; i < outputs; i++) {	

				if(legalMoves[i%(int)Math.pow(inputs,0.5)][i/(int)Math.pow(inputs,0.5)]!=0&&(finals[minMaxIndex]>finals[i]|legalMoves[minMaxIndex%(int)Math.pow(inputs,0.5)][minMaxIndex/(int)Math.pow(inputs,0.5)]==0)) {
					minMaxIndex = i;
				}
			}	
		}
		if(legalMoves[minMaxIndex%(int)Math.pow(inputs,0.5)][minMaxIndex/(int)Math.pow(inputs,0.5)]==0) {
			o.pass();
		}else {
			o.move(minMaxIndex%(int)Math.pow(inputs,0.5), minMaxIndex/(int)Math.pow(inputs,0.5), minMax);
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
