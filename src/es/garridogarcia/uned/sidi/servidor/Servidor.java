package es.garridogarcia.uned.sidi.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import es.garridogarcia.uned.sidi.common.Gui;
import es.garridogarcia.uned.sidi.common.ServicioAutenticacionInterface;
import es.garridogarcia.uned.sidi.common.ServicioDatosInterface;
import es.garridogarcia.uned.sidi.common.ServicioGestorInterface;
import es.garridogarcia.uned.sidi.common.Utils;

/**
 * Esta clase contiene el método principal de la entidad Servidor.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public class Servidor {
	
	// Almacena el objeto del servicio de autenticacion
	private static ServicioAutenticacionImpl servAutenticacion;
	// Almacena el objeto del servicio gestor
	private static ServicioGestorImpl servGestor;
	// Almacena la interfaz remota del servicio de datos
	private static ServicioDatosInterface servDatos;
	
	/**
	 * Método principal de la entidad Servidor.
	 * El método establece la propiedad de codebase, obtiene el servicio de datos
	 * conectando con la entidad Base de Datos y crea los objetos del servicio
	 * de autenticación y del servicio gestor. Una vez creados los servicios los
	 * arranca y muestra por pantalla una interfaz de texto con diversas opciones.
	 * @param args
	 */
	public static void main (String[] args) {
		
		Utils.setCodebase(ServicioAutenticacionInterface.class);
		Utils.setCodebase(ServicioGestorInterface.class);
		Utils.setHostname();
		
		obtenerServicioDatos();
		iniciarServicios();
		mostrarInterfaz();		
	}
	
	/**
	 * Obtiene la interfaz remota del servicio de datos, guardándola en la variable
	 * estática servDatos. Inicialmente busca el servicio asumiendo que está ejecutándose
	 * localmente en la misma máquina, si no es así, pide al usuario la información de 
	 * conexión por pantalla.
	 */
	private static void obtenerServicioDatos() {
		
		String URLRegistroServDatos = "rmi://" + Utils.getHostAddress() + ":1099"
 				+ "/" + ServicioDatosInterface.NOMBRE_SERVICIO + "/";
		
		try {
			servDatos = (ServicioDatosInterface) Naming.lookup(URLRegistroServDatos);
		} catch (MalformedURLException e) {
			System.out.println(
					"Error. La URL está bien formateada.");
			e.printStackTrace();
		} catch (RemoteException | NotBoundException e) {
			while (true) {
				String[] opt = Gui.input("Datos de conexión a la Base de Datos",
						            new String[] {"Introduzca la dirección IP de la Base de datos: ",
						            		      "Introduzca el puerto del registro RMI: "});
				
				URLRegistroServDatos = "rmi://" + opt[0] + ":" + opt[1] + "/" 
				                       + ServicioDatosInterface.NOMBRE_SERVICIO + "/";
				
				try {
					servDatos = (ServicioDatosInterface) Naming.lookup(URLRegistroServDatos);
					break;
				} catch (Exception e1) {
					System.out.println(
							"Error. Compruebe que el servicio de base de datos se ha iniciado y que los "
							 + "datos de conexión que ha introducido son correctos.");
				}
			}
		}
	}
	
	private static void iniciarServicios() {
		
		try {
			servGestor = new ServicioGestorImpl(servDatos);
			servAutenticacion = new ServicioAutenticacionImpl(servDatos,servGestor);
		} catch (RemoteException e) {
			System.out.println("Error al crear los objetos remotos.");
			e.printStackTrace();
		}
		servAutenticacion.enlazarServicio();
		servGestor.enlazarServicio();
	}
	
	// Muestra la interfaz de texto
	private static void mostrarInterfaz() {
		
		int opt = 0;
		
		do {
			opt = Gui.menu("Menú Principal Servidor", new String[] {"Información del Servidor",
																	"Listar Usuarios Logeados",
																   	"Salir"});
			switch (opt) {
				case 0: imprimirInfoServicios(); break;
				case 1: imprimirNicksLogeados(); break;
				case 2: detenerServicios(); break;
			}
		} while (opt != 2);
	}
	
	// Imprime las URL de los servicios de la entidad Servidor	
	private static void imprimirInfoServicios() {
		
		System.out.println(servAutenticacion.obtenerURLRegistro());
		System.out.println(servGestor.obtenerURLRegistro());
	}
	
	// Imprime los nicks de los usuario logeados en el sistema
	private static void imprimirNicksLogeados() {
		
		List<String> nicks = null;
		
		try {
			nicks = servDatos.obtenerNicksLogeados();
		} catch (RemoteException e) {
			System.out.println("Error al realizar llamada remota.");
			e.printStackTrace();
		}
		
		if (nicks.isEmpty()) {
			System.out.println("No hay usuarios logeados.");
		} else {
			for (String nick : nicks) {
				System.out.printf("> %s\n", nick);
			}
		}
	}
	
	// Desenlaza los objetos remotos de los servicios del servidor y los elimina
	private static void detenerServicios() {
		
		servAutenticacion.desenlazarServicio();
		servGestor.desenlazarServicio();
		servAutenticacion.eliminarServicio();
		servGestor.eliminarServicio();
	}
}
