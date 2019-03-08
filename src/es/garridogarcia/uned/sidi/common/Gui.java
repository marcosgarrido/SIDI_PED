package es.garridogarcia.uned.sidi.common;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class Gui {
	
	private static Console console = System.console();
	private static BufferedReader reader = new BufferedReader(
											new InputStreamReader(System.in));
	
	public static String[] input(String name, String[] msgs) {
		
		newLine();
		
		String[] inputs = new String[msgs.length];
		
		outLn("=== " + name + " ===");
		
		newLine();
		
		for (int i = 0; i < msgs.length; i++) {
			inputs[i] = input(msgs[i]);
		}
		newLine();
		newLine();
		
		return inputs;
	}
	
	public static String input(String name, String msg) {

		newLine();
		
		outLn("=== " + name + " ===");
		
		newLine();
		
		return input(msg);
	}
	
	private static String input(String msg) {
		
		out(msg);
		String line = readLine();
		
	    return line;
	}
	
	public static int menu(String name, String[] entradas) {
		
		newLine();
		
		outLn("=== " + name + " ===");
		newLine();
		outLn("Seleccione una opcion:\n"
				+ "");
		
		for (int i = 0; i < entradas.length; i++) {
			outLn((i + 1) + ".- " + entradas[i]);
		}
		
		newLine();
		
		int opt = -1;
		do {
			try {
				opt = Integer.parseInt(readLine().trim());
			} catch(NumberFormatException e) {
				opt = -1;
			}			
			
			if (opt - 1 >= entradas.length || opt <= 0) {
				outLn("Ingrese una opcion del 1 al " + entradas.length);
				opt = -1;
			}
		}
		while(opt == -1);
		
		newLine();
		
		return opt - 1;
	}
	
	public static boolean preguntar(String pregunta) {
		
		newLine();
		
		Boolean opt = null;
		
		do {
			out(pregunta + " [s/n] ");
			String yn = readLine();
			
			if (yn.startsWith("s")) opt = true;
			if (yn.startsWith("n")) opt = false;
		}
		while (opt == null);
		
		newLine();
		
		return opt;
	}
	
	private static void outLn(String msg) {
		System.out.println(msg);
	}
	
	private static void newLine() {
		System.out.println();
	}
	
	private static void out(String msg) {
		System.out.print(msg);
	}
	
	private static String readLine() {
		if (console != null) return console.readLine();
		
		try {
			return reader.readLine();
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
