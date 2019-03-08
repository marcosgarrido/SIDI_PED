package es.garridogarcia.uned.sidi.basededatos;

import java.rmi.RemoteException;
import java.util.List;

import es.garridogarcia.uned.sidi.common.Gui;
import es.garridogarcia.uned.sidi.common.ServicioDatosInterface;
import es.garridogarcia.uned.sidi.common.Trinador;
import es.garridogarcia.uned.sidi.common.Trino;
import es.garridogarcia.uned.sidi.common.Utils;

/**
 * Esta clase contiene el método principal de la entidad Base de Datos.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public class Basededatos {
	
	
	// Almacena el objeto del servicio de datos
	public static ServicioDatosImpl servDatos;
	

	/**
	 * Método principal de la entidad Base de Datos.
	 * Crea un objeto de servicio de datos, arranca el servicio de datos y 
	 * muestra una interfaz de texto por pantalla con diversas opciones.
	 * @param args
	 */
	public static void main (String[] args) {
		
		Utils.setCodebase(ServicioDatosInterface.class);
		Utils.setHostname();
		
		iniciarServicio();		
		mostrarInterfaz();
	}
	
	private static void iniciarServicio() {
		
		try {
			servDatos = new ServicioDatosImpl();
		} catch (RemoteException e) {
			System.out.println("Error al crear objeto remoto");
			e.printStackTrace();
		}
		servDatos.enlazarServicio();
	}
	
	// Muestra la interfaz de texto
	private static void mostrarInterfaz() {
		
		int opt = 0;
		
		do {
			opt = Gui.menu("Menú Principal Base de Datos", new String[] {"Información de la Base de Datos",
																		 "Listar Usuarios Registrados",
																		 "Listar Trinos",
																	 	 "Salir"});
			switch (opt) {
				case 0: imprimirInfoBasededatos(); break;
				case 1: imprimirNicksRegistrados(); break;
				case 2: imprimirTrinos(); break;
				case 3: salir();
			}
		} while (opt != 3);
	}
	
	// Imprime por pantalla la URL del objeto del servicio de datos
	private static void imprimirInfoBasededatos() {
		
		System.out.println(servDatos.obtenerURLRegistro());
	}
	
	// Imprime los nicks de los usuarios registrados en el sistema.
	private static void imprimirNicksRegistrados() {
		
		List<Trinador> trinadores = null;
		
		try {
			trinadores = servDatos.obtenerUsuarios();
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto."); 
			e.printStackTrace();
		}
		
		if (trinadores.isEmpty()) {
			System.out.println("No hay usuarios registrados.");
		} else {
			for (Trinador t : trinadores) {
				System.out.printf("> %s\n", t.getNick());
			}
		}
	}
	
	// Imprime los trinos del sistema
	private static void imprimirTrinos() {
		
		List<Trino> trinos = servDatos.obtenerTrinos();
		
		if (trinos.isEmpty()) {
			System.out.println("No hay trinos.");
		} else {
			for (Trino trino : trinos) {
				System.out.printf("> %s # %s\n", 
						trino.ObtenerNickPropietario(), 
						trino.ObtenerTrino());
			}
		}
	}
	
	// Desenlaza el objeto remoto del servicio de datos del registro RMI y lo elimina.
	private static void salir() {
		
		servDatos.desenlazarServicio();
		servDatos.eliminarServicio();
	}

}
