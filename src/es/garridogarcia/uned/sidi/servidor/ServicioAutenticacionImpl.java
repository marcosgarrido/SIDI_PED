
package es.garridogarcia.uned.sidi.servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import es.garridogarcia.uned.sidi.common.CallbackUsuarioInterface;
import es.garridogarcia.uned.sidi.common.Servicio;
import es.garridogarcia.uned.sidi.common.ServicioAutenticacionInterface;
import es.garridogarcia.uned.sidi.common.ServicioDatosInterface;
import es.garridogarcia.uned.sidi.common.ServicioGestorInterface;
import es.garridogarcia.uned.sidi.common.Trinador;

/**
 * Esta clase implementa la interfaz remota del servicio de autenticacion y extiende la
 * clase Servicio. Se encarga de registrar, identificar y cerrar la sesión de los usuarios
 * del sistema.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
@SuppressWarnings("serial")
public class ServicioAutenticacionImpl extends Servicio implements ServicioAutenticacionInterface {
	
	// Almacena la interfaz remota del servicio de datos
	private ServicioDatosInterface servDatos;
	// Almacena la interfaz remota del servicio gestor
	private ServicioGestorInterface servGestor;
	
	/**
	 * Constructor.
	 * El constructor de la clase llama al constructor de la superclase, pasándole como parámetros
	 * el nombre del servicio, el puerto rmi del registro y el puerto del servicio, que son declarados
	 * en la interfaz implementada por la clase.
	 */
	protected ServicioAutenticacionImpl(ServicioDatosInterface servDatos, 
			ServicioGestorInterface servGestor) throws RemoteException {
		
		super(NOMBRE_SERVICIO, PUERTO_RMI, PUERTO_SERVICIO);
		this.servDatos = servDatos;
		this.servGestor = servGestor;
	}
	
	/**
	 * Registra un usuario en el sistema de trinos.
	 * @param nombre El nombre del usuario
	 * @param nick El nick del usuario
	 * @param password La contraseña del usuario
	 * @return Número entero que codifica el resultado de la operacion:
	 * 			0 - Éxito
	 * 			1 - Error. El usuario ya existe en el sistema
	 * @throws RemoteException
	 */
	@Override
	public int registrarUsuario (String nombre, String nick, String password) throws RemoteException {
		
		Trinador trinador = new Trinador(nombre,nick,password);
		List<Trinador> trinadores = servDatos.obtenerUsuarios();
		
		if (trinadores.contains(trinador)) return 1;
		
		servDatos.almacenarUsuario(trinador);
		
		return 0;
	}
	
	/**
	 * Identifica un usuario del sistema de trinos. Realiza a su vez unan llamada remota
	 * al servicio gestor para que transmita los trinos pendientes de recibir al usuario
	 * que se identifica.
	 * @param nick El nick del usuario a identificar
	 * @param password La contraseña del usuario
	 * @param callback El servicio de callback del usuario que trata de identificarse
	 * @return Número entero que codifica el resultado de la operación:
	 * 			0 - Éxito 
	 * 			1 - Error. El nick de usuario no se encuentra registrado en el sistema
	 * 			2 - Error. La contraseña es incorrecta
	 * @throws RemoteException
	 */
	@Override
	public int identificarUsuario (String nick, String password, CallbackUsuarioInterface callback) throws RemoteException {
		
		if (servDatos.obtenerNicksLogeados().contains(nick)) return 3;
		
		boolean encontrado = false;
		
		for (Trinador t : servDatos.obtenerUsuarios()) {
			if (t.getNick().toLowerCase().equals(nick.toLowerCase())) {
				if (!t.getPassword().equals(password)) return 2;
				encontrado = true;
				servDatos.almacenarCallbackPorNickLogeado(nick,callback);
			}
		}		
		
		if (encontrado) {
			servGestor.transmitirTrinos(nick);
			return 0;
		} else return 1;
	}
	
	/**
	 * Devuelve una lista con los nicks de los usuarios registrados del sistema.
	 * @return Lista de nicks
	 * @throws RemoteException
	 */
	@Override
	public List<String> obtenerNicksSistema() throws RemoteException {
		
		List<String> nicks = new ArrayList<String>();
		
		for (Trinador t : servDatos.obtenerUsuarios())
			nicks.add(t.getNick());
		
		return nicks;
	}
	
	/**
	 * Cierra la sesión de un usuario, dejando de estar logeado.
	 * @param nick El nick del usuario a cerrar sesión
	 * @throws RemoteException
	 */
	@Override
	public void cerrarSesion (String nick) throws RemoteException {
		
		servDatos.eliminarCallbackPorNickLogeado(nick);
	}
}
