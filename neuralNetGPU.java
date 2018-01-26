public class neuralNetGPU extends neuralNet{
	final JCL gpu;
	public neuralNetGPU(int inputs, int hiddenLayers, int neuronsPerLayer, int outputs,JCL gpu) {
		super(inputs, hiddenLayers, neuronsPerLayer, outputs);
		this.gpu = gpu;
	}

	public void takeTurn(OthelloBoard o, boolean minMax){
		System.out.println("makinMove");
		Boolean[][] values = o.getBoard();
		int[][] legalMoves = o.getLegalMoves();
		float[] in = new float[neuronsPerLayer];
		float[] out = new float[neuronsPerLayer];
		float[] finals = new float[outputs];
		float[] buff = new float[inputs+1];
		float[] buff1;
		int minMaxIndex = 0;
		for(int x = 0; x < neuronsPerLayer; x++) { 			
			for(int y = 0; y < inputs; y++) {
				buff[y]	= values[y%8][y/8]==null?0:(values[y%8][y/8]?1:-1);
			}
			buff[inputs] = 1;
			buff1 = gpu.multiply(buff, inputLayer[x]);
			for(float i:buff1) {
				in[x]+=i;
			}
		}

		//for(int x = 0; x < neuronsPerLayer; x++) {
		//	for(int y = 0; y < inputs; y++) {
		//		in[x] += inputLayer[x][y] * (values[y%8][y/8]==null?0:(values[y%8][y/8]?1:-1));
		//	}
		//	in[x] += inputLayer[x][inputs];
		//}

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

}
