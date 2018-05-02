import java.io.*;

public class dataProcess {

	public static void main(String[] args) {
		String out = "";
		out += "neuronsPerLayer,hiddenLayers,problem,gen\n";
		for(int problem = 0; problem < 1; problem++)
		for(int neuronsPerLayer = 1; neuronsPerLayer <= 8;neuronsPerLayer++) 
			for(int hiddenLayers = 1; hiddenLayers <= 8;hiddenLayers++) {				
				try (BufferedReader file =	new BufferedReader(new InputStreamReader(new FileInputStream(String.format(".\\src\\csvs\\L%dNPL%dp%d.csv",hiddenLayers,neuronsPerLayer,problem))))) {
					while(file.ready()) {
						String[] lineStr = file.readLine().split(",");
						out += neuronsPerLayer + "," + hiddenLayers + "," + problem + "," + lineStr[0] + "\n";
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}			
				
			}
		try (FileOutputStream fos =	new FileOutputStream(".\\src\\processed\\5d.csv")) {			
			
			fos.write(out.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		

	}

}
