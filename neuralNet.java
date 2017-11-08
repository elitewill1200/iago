public class neuralNet{
	double[][] inputLayer;
	double[][][] hiddenNet;
	double[][] outputLayer;
	int inputs,hiddenLayers,neuronsPerLayer,outputs;
	public neuralNet(int inputs, int hiddenLayers, int neuronsPerLayer, int outputs){
		this.inputs = inputs;
		this.hiddenLayers = hiddenLayers;
		this.neuronsPerLayer = neuronsPerLayer;
		this.outputs = outputs;

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
	public int takeTurn(Boolean[] values, boolean minMax){
		double[] in = new double[neuronsPerLayer];
		double[] out = new double[neuronsPerLayer];
		double[] finals = new double[outputs];
		int minMaxIndex = 0;
		for(int x = 0; x < neuronsPerLayer; x++) {
			for(int y = 0; y < inputs; y++) {
				in[x] += inputLayer[x][y] * (values[y]==null?0:(values[y]?1:-1));
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
			finals[x] += inputLayer[x][inputs];
		}
		
		if(minMax) {
			for(int i = 1; i < outputs; i++) {
				if(values[i]==null&&(finals[minMaxIndex]<finals[i]||values[minMaxIndex]!=null)) {
					minMaxIndex = i;
				}
			}			
		}else {
			for(int i = 1; i < outputs; i++) {				
				if(values[i]==null&&(finals[minMaxIndex]>finals[i]||values[minMaxIndex]!=null)) {
					minMaxIndex = i;
				}
			}	
		}
		return minMaxIndex;
	}
}
