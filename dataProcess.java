import java.io.*;

public class dataProcess {

	public static void main(String[] args) {
		for(int neuronsPerLayer = 1; neuronsPerLayer <= 4;neuronsPerLayer++) 
			for(int hiddenLayers = 1; hiddenLayers <= 8;hiddenLayers++) {
				int[][] count = new int[150][201];
				try (BufferedReader file =	new BufferedReader(new InputStreamReader(new FileInputStream(String.format(".\\src\\csvs\\L%dNPL%d.csv",hiddenLayers,neuronsPerLayer))))) {
					while(file.ready()) {
						String[] lineStr = file.readLine().split(",");
						int[] line = new int[] {Integer.parseInt(lineStr[0]),(int) (Double.parseDouble(lineStr[1]) * 2)};
						count[line[0]][line[1]]++;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try (FileOutputStream fos =	new FileOutputStream(String.format(".\\src\\processed\\L%dNPL%d.csv",hiddenLayers,neuronsPerLayer))) {
					String out = "";
					//out += "gen,score,count\n";
					for(int gen = 0; gen < 150; gen++)
						for(int number = 0; number <201; number++)
							out += gen + "," + number/2.0 + "," + count[gen][number] + "\n";
					fos.write(out.getBytes());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

	}

}
