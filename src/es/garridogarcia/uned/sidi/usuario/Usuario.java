package es.garridogarcia.uned.sidi.usuario;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import es.garridogarcia.uned.sidi.common.Gui;
import es.garridogarcia.uned.sidi.common.ServicioAutenticacionInterface;
import es.garridogarcia.uned.sidi.common.ServicioGestorInterface;
import es.garridogarcia.uned.sidi.common.Utils;

/**
 * Esta clase contiene el método principal de la entidad Cliente.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public class Usuario {
	
	// Almacena la interfaz remota del servicio de autenticacion
	private static ServicioAutenticacionInterface servAutenticacion;
	// Almacena la interfaz remota del servicio gestor
	private static ServicioGestorInterface servGestor;
	// Almacena el objeto del servicio de callback de usuario
	private static CallbackUsuarioImpl callbackUsuario;
	// Almacena el nick del usuario que se está utilizando en el cliente
	private static String nickUsuario;
	
	/**
	 * Método principal de la entidad Cliente.
	 * El método establece la propiedad de codebase, obtiene las interfaces
	 * remotas de los servicios de autenticación y gestión de la entidad
	 * Servidor y muestra la interfaz de texto del cliente con sus opciones.
	 * @param args
	 */
	public static void main (String[] args) {
		
		Utils.setCodebase(ServicioAutenticacionInterface.class);
		Utils.setCodebase(ServicioGestorInterface.class);
		Utils.setHostname();
		
		obtenerServiciosServidor();
	    
		mostrarInterfaz();
	}
	
	/**
	 * Obtiene las interfaces remotas de los servicios de autenticación y gestión
	 * almacenándolas en las variables servAutenticacion y servGestor respectivamente.
	 * Inicialmente busca los servicios asumiendo que están ejecutándose localmente
	 * en la misma máquina, si no es así, pide al usuario la información de conexión
	 * por pantalla.
	 */
	private static void obtenerServiciosServidor() {
		
		String URLRegistroServAutenticacion = "rmi://" + Utils.getHostAddress() + ":1099"
 											  + "/" + ServicioAutenticacionInterface.NOMBRE_SERVICIO
 											  + "/";
		
		String URLRegistroServGestor = "rmi://" + Utils.getHostAddress() + ":1099"
				  					    + "/" + ServicioGestorInterface.NOMBRE_SERVICIO
				  					    + "/";
		
		try {
			servAutenticacion = (ServicioAutenticacionInterface) Naming.lookup(URLRegistroServAutenticacion);
			servGestor = (ServicioGestorInterface) Naming.lookup(URLRegistroServGestor);
		} catch (MalformedURLException e) {
			System.out.println(
					"Error. La URL está bien formateada.");
			e.printStackTrace();
		} catch (RemoteException | NotBoundException e) {
			while (true) {
				String[] opt = Gui.input("Datos de conexión al Servidor",
						            new String[] {"Introduzca la dirección IP del Servidor: ",
						            		      "Introduzca el puerto del registro RMI: "});
				
				URLRegistroServAutenticacion = "rmi://" + opt[0] + ":" + opt[1] + "/" 
				                       			+ ServicioAutenticacionInterface.NOMBRE_SERVICIO + "/";
				
				URLRegistroServGestor = "rmi://" + opt[0] + ":" + opt[1] + "/" 
               							+ ServicioGestorInterface.NOMBRE_SERVICIO + "/";
				
				try {
					servAutenticacion = (ServicioAutenticacionInterface) Naming.lookup(URLRegistroServAutenticacion);
					servGestor = (ServicioGestorInterface) Naming.lookup(URLRegistroServGestor);
					break;
				} catch (Exception e1) {
					System.out.println(
							"Error. Compruebe que los servicios del servidor se han iniciado y que los "
							 + "datos de conexión que ha introducido son correctos.");
				}
			}
		}
	}
	
	/**
	 * Muestra la interfaz inicial del usuario, con las opciones de registrar un usuario en
	 * el sistema, identificar un usuario o salir.
	 */
	private static void mostrarInterfaz() {
		
		int opt = Gui.menu("Login de Usuario", new String[] {"Registrar un nuevo usuario",
			     											 "Hacer login",
															 "Salir"});
		switch (opt) {
			case 0: registrarUsuario(); break;
			case 1: identificarUsuario(); break;
			case 2: return;
		}
	}
	
	/**
	 * Muestra el menú de registro de usuario, pidiendo por pantalla el nombre del usuario,
	 * el nick y la contraseña. Se realiza una llamada remota al servicio de autenticacion
	 * para registrar el usuario. Se informa al usuario por pantalla si el registro se ha
	 * realizado con éxito o si por el contrario el nick escogido ya ha sido utilizado por
	 * otro usuario.
	 */
	private static void registrarUsuario() {
		
		String[] opt = Gui.input("Menú de Registro de Usuario", new String[] {"Introduzca su nombre: ", 
				"Introduzca su nick: ",
				"Introduzca su contraseña: "});

		for (String s : opt) {
			if (s == null || s.isEmpty()) {
				System.out.println("Entrada de datos incorrecta");
				if (Gui.preguntar("Volver a intentar?")) {
					registrarUsuario();
				} else {
					mostrarInterfaz();
				}
				return;
			}
		}

		int respuesta = 0;
		
		try {
			respuesta = servAutenticacion.registrarUsuario(opt[0],opt[1],opt[2]);
		} catch (RemoteException e) {
			System.out.println("Error al realizar llamada remota.");
			e.printStackTrace();
		}

		switch (respuesta) {
			case 0: 
				System.out.println("Registro completado. Ya puede logearse en el sistema.");
				mostrarInterfaz();
				break;
			case 1: 
				System.out.println("El nick que ha elegido no está disponible.");
				if (Gui.preguntar("Volver a intentar?")) {
					registrarUsuario();
				} else {
					mostrarInterfaz();
				}
				break;
		}	
	}
	
	/**
	 * Muestra el menú para identificar a un usuario, pidiendo por pantalla el nick del
	 * usuario y la contraseña. Se realiza una llamada remota al servicio de autenticación
	 * para identificar al usuario, mandandole el nick, la contraseña y el servicio de
	 * callback que el usuario utilizará para recibir automáticamente los trinos. Se
	 * informa al usuario cuando el nick que ha introducido no existe en el sistema, cuando
	 * existe pero la contraseña es incorrecta o cuando el usuario ya se encuentra logeado.
	 */
	private static void identificarUsuario() {
		
		String[] opt = Gui.input("Menú de Identificación de Usuario", 
				new String[]{"Ingrese su nick: ",
							"Ingrese su contraseña: "});
		
		for (String s : opt) {
			if (s == null || s.isEmpty()) {
				System.out.println("Entrada de datos incorrecta.");
				if (Gui.preguntar("Volver a intentar?")) {
					registrarUsuario();
				} else {
					mostrarInterfaz();
				}
				return;
			}
		}
	
		nickUsuario = opt[0];

		int respuesta = 0;
		boolean identificado = false;
		
		try {
			callbackUsuario = new CallbackUsuarioImpl(nickUsuario);
			respuesta = servAutenticacion.identificarUsuario(opt[0], opt[1], callbackUsuario);
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto.");
			e.printStackTrace();
		}
		
		switch (respuesta) {
			case 0: identificado = true;
					callbackUsuario.enlazarServicio();
					mostrarMenuPrincipal();
					break;
			case 1: System.out.println(
				"El nick que ha introducido no existe.");
				break;
			case 2: System.out.println(
				"La contraseña introducida no es correcta.");
				break;
			case 3: System.out.println(
				"El usuario ya se encuentra logeado.");
				break;
		}
		
		if (!identificado && Gui.preguntar("Volver a intentar?")) {
			identificarUsuario();
		} else {
			mostrarInterfaz();
		}
	}
	
	// Muestra el menú principal del usuario una vez se identifica
	private static void mostrarMenuPrincipal() {
		
		int opt = Gui.menu("Menú Principal Usuario", new String[] {"Información del Usuario",
			   	"Enviar Trino",
			   	"Listar Usuarios del Sistema",
			   	"Seguir a",
			   	"Dejar de seguir a",
			   	"Borrar trino a los usuarios que todavía no lo han recibido",
				"Salir \"Logout\""});
		switch (opt) {
			case 0: imprimirInfoUsuario(); break;
			case 1: enviarTrino(); break;
			case 2: imprimirUsuariosSistema(); break;
			case 3: seguirA(); break;
			case 4: dejarDeSeguirA(); break;
			case 5: borrarTrino(); break;
			case 6: hacerLogout(); break;
		}
	}
	
	// Imprime por pantalla la URL del servicio de callback de usuario
	private static void imprimirInfoUsuario() {
		
		System.out.println(callbackUsuario.obtenerURLRegistro());
		
		mostrarMenuPrincipal();
	}
	
	/**
	 *  Muestra el menú de envío de trinos, solicitando el trino al usuario y
	 *  haciendo la correspondiente llamada remota al servicio gestor.
	 */
	private static void enviarTrino() {
		
		String trino = Gui.input("Enviar Trino", "Inserte trino a enviar: ");
		
		if (trino != null && !trino.isEmpty()) {
			try {
				long id = servGestor.enviarTrino(trino, nickUsuario);
				System.out.println("\nTrino enviado. Su identificador es " + id);
			} catch (RemoteException e) {
				System.out.println(
						"Error. No ha podido enviarse el trino.");
				e.printStackTrace();
			}
		} else {
			System.out.println("\nError. No puede enviarse un trino vacío.");
		}

		mostrarMenuPrincipal();
	}
	
	/**
	 *  Imprime por pantalla los nicks de los usuarios registrados en el sistema,
	 *  obtenidos mediante una llamada remota al servicio de autenticación.
	 */
	private static void imprimirUsuariosSistema() {
		
		List<String> nicks = null;
		
		try {
			nicks = servAutenticacion.obtenerNicksSistema();
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto."); 
			e.printStackTrace();
		}
		
		for (String nick : nicks)
			System.out.printf("> %s\n", nick);
		
		mostrarMenuPrincipal();
	}
	
	/**
	 * Muestra el menu Seguir A. Se solicita al usuario el nick a seguir y se realiza una
	 * llamada remota al servicio gestor para que se encargue de agregarlo. Se informa al 
	 * usuario si el nick a seguir no existe en el sistema. Si el nick a seguir ya se seguía
	 * previamente no se realiza ningún cambio.
	 */
	private static void seguirA() {
		
		String nickSeguido = Gui.input("Seguir a", "Inserte el nick del usuario a seguir: ");
		
		int respuesta = 0;
		
		try {
			respuesta = servGestor.seguirA(nickUsuario,nickSeguido);
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto."); 
			e.printStackTrace();
		}
		
		switch (respuesta) {
			case 1: 
				System.out.println(
					"El usuario que ha ingresado no existe en el sistema.");
				if (Gui.preguntar("Volver a intentar?"))
					seguirA();
				break;
			case 2:
				System.out.println(
					"Ha ingresado su propio nick");
				if (Gui.preguntar("Volver a intentar?"))
					seguirA();
				break;
		}
		mostrarMenuPrincipal();
	}
	
	/**
	 * Muestra el menu Dejar de Seguir A. Se solicita al usuario el nick a dejar de seguir
	 * y se realiza una llamada remota al servicio gestor para que se encargue de eliminarlo
	 * como seguidor. Se informa al usuario si el nick a dejar de seguir no existe en el
	 * sistema. Si el nick a dejar de seguir no se seguía previamente no se realiza ningún
	 * cambio.
	 */
	private static void dejarDeSeguirA() {
		
		String nickSeguido = Gui.input("Dejar de seguir a", "Inserte el nick del usuario a dejar seguir: ");
		
		int respuesta = 0;
		
		try {
			respuesta = servGestor.dejarDeSeguirA(nickUsuario,nickSeguido);
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto."); 
			e.printStackTrace();
		}
		
		switch (respuesta) {
			case 1: 
				System.out.println(
					"El usuario que ha ingresado no existe en el sistema.");
				if (Gui.preguntar("Volver a intentar?"))
					dejarDeSeguirA();
				break;
			case 2:
				System.out.println(
					"Ha ingresado su propio nick");
				if (Gui.preguntar("Volver a intentar?"))
					dejarDeSeguirA();
				break;
		}
		mostrarMenuPrincipal();
	}
	
	/**
	 * Muestra el menú Borrar Trino. El usuario introduce el identificador único del trino que
	 * quiere borrar para los usuarios que le siguen y que aún no han recibido por estar 
	 * desconectados, realizándose la correspondiente llamada remota al servicio gestor.
	 */
	private static void borrarTrino() {
		
		String sid = Gui.input("Borrar trino", "Inserte el identificador del trino a borrar: ");
		
		try {
			servGestor.eliminarTrinoBuffers(sid);
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto."); 
			e.printStackTrace();
		}
		mostrarMenuPrincipal();
	}
	
	/**
	 * Realiza una llamada remota al servicio de autenticación para notificar que el usuario
	 * ha cerrado la sesión, desenlaza el objeto remoto de callback del registro RMI y lo elimina.
	 */
	private static void hacerLogout() {
		
		try {
			servAutenticacion.cerrarSesion(nickUsuario);
		} catch (RemoteException e) {
			System.out.println(
					"Error al realizar la llamada al procedimiento remoto.");
			e.printStackTrace();
		}
		callbackUsuario.desenlazarServicio();
		callbackUsuario.eliminarServicio();
	}
}
	
