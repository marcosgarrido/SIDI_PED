package es.garridogarcia.uned.sidi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Interfaz remota del servicio de datos de la entidad Base de Datos.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public interface ServicioDatosInterface extends Remote {
	
	// Almacena el nombre del servicio
	public static final String NOMBRE_SERVICIO = "ServicioDatos";
	// Almacena el puerto donde escuchará el servicio
	public static final int PUERTO_SERVICIO = 8000;
	// Almacena el puerto del registro RMI por defecto
	public static final int PUERTO_RMI = 1099;
	
	/**
	 * Devuelve la lista de nicks logeados en el sistema.
	 * @return Lista de nicks
	 * @throws RemoteException
	 */
	public List<String> obtenerNicksLogeados() throws RemoteException;
	
	/**
	 * Devuelve la lista de usuarios registrados en el sistema.
	 * @return Lista de usuarios
	 * @throws RemoteException
	 */
	public List<Trinador> obtenerUsuarios() throws RemoteException;
	
	/**
	 * Almacena un usuario en el sistema, creando su lista de seguidores
	 * y su buffer de trinos.
	 * @param t Usuario a almacenar
	 * @throws RemoteExcepction
	 */
	public void almacenarUsuario(Trinador t) throws RemoteException;
	
	/**
	 * Almacena el nick de un usuario logeado con su callback.
	 * @param nick El nick del usuario
	 * @param callback Interfaz remota de callback del usuario
	 * @throws RemoteException
	 */
	public void almacenarCallbackPorNickLogeado(String nick, CallbackUsuarioInterface callback) throws RemoteException;
	
	/**
	 * Elimina el nick y el callback de un usuario del almacén de usuarios logeados.
	 * @param nick El nick del usuario
	 * @throws RemoteException
	 */
	public void eliminarCallbackPorNickLogeado(String nick) throws RemoteException;
	
	/**
	 * Almacena un trino en el almacén de trinos.
	 * @param t Trino a almacenar
	 * @throws RemoteException
	 */
	public void almacenarTrino(Trino t) throws RemoteException;
	
	/**
	 * Almacena un trino en el buffer de un usuario.
	 * @param t Trino a almacenar
	 * @param nick El nick del usuario
	 * @throws RemoteException
	 */
	public void almacenarTrinoBuffer(Trino t, String nick) throws RemoteException;
	
	/**
	 * Devuelve una lista con los nicks que siguen a un usuario.
	 * @param nick El nick de usuario del que obtener los nicks seguidores
	 * @return Lista de nicks de los seguidores
	 */
	public List<String> obtenerSeguidores(String nick) throws RemoteException;
	
	/**
	 * Devuelve el mapa que relaciona los nicks de los usuarios logueados con sus respectivas
	 * interfaces remotas.
	 * @return Mapa que relaciona nicks logeados con sus interfaces remotas
	 * @throws RemoteException
	 */
	public Map<String,CallbackUsuarioInterface> obtenerCallbackPorNickLogeado() throws RemoteException;
	
	/**
	 * Almacena un nick en la lista de seguidores de otro nick.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @throws RemoteException
	 */
	public void almacenarSeguidor(String nickSeguidor, String nickSeguido) throws RemoteException;
	
	/**
	 * Elimina un nick de la lista de seguidores de otro nick.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick de usuario que es seguido
	 * @throws RemoteException
	 */
	public void eliminarSeguidor(String nickSeguidor, String nickSeguido) throws RemoteException;
	
	/**
	 * Devuelve el buffer de trinos de un usuario.
	 * @param nick El nick de usuario del cual queremos recuperar el buffer
	 * @return El buffer del usuario que es una lista de trinos
	 * @throws RemoteException
	 */
	public List<Trino> obtenerBufferUsuario(String nick) throws RemoteException;
	
	/**
	 * Devuelve la interfaz remota de callback del usuario cuyo nick se pasa como
	 * parámetro.
	 * @param nick El nick del usuario del cual queremos obtener la interfaz remota.
	 * @return La interfaz remota del callback del usuario.
	 * @throws RemoteException
	 */	
	public CallbackUsuarioInterface obtenerCallbackUsuario(String nick) throws RemoteException;
	
	/**
	 * Elimina un trino de los buffers de usuario.
	 * @param id El identificador único del trino a borrar
	 * @throws RemoteException
	 */
	public void eliminarTrinoBuffers(long id) throws RemoteException;

}
