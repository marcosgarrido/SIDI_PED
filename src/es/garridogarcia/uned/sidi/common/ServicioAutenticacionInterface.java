package es.garridogarcia.uned.sidi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota del servicio de autenticación de la entidad Servidor.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public interface ServicioAutenticacionInterface extends Remote {
	
	// Almacena el nombre del servicio
	public static final String NOMBRE_SERVICIO = "ServicioAutenticacion";
	// Almacena el puerto donde escuchará el servicio
	public static final int PUERTO_SERVICIO = 8001;
	// Almacena el puerto del registro RMI por defecto
	public static final int PUERTO_RMI = 1099;
	
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
	public int registrarUsuario(String nombre, String nick, String password) throws RemoteException;
	
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
	public int identificarUsuario(String nick, String password, CallbackUsuarioInterface callback) throws RemoteException;
	
	/**
	 * Devuelve una lista con los nicks de los usuarios registrados del sistema.
	 * @return Lista de nicks
	 * @throws RemoteException
	 */
	public List<String> obtenerNicksSistema() throws RemoteException;
	
	/**
	 * Cierra la sesión de un usuario, dejando de estar logeado.
	 * @param nick Cadena con el nick del usuario a cerrar sesión
	 * @throws RemoteException
	 */
	public void cerrarSesion(String nick) throws RemoteException;

}
