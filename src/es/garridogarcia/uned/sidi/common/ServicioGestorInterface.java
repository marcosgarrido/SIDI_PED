package es.garridogarcia.uned.sidi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del servicio gestor de la entidad Servidor.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public interface ServicioGestorInterface extends Remote {
	
	// Almacena el nombre del servicio
	public static final String NOMBRE_SERVICIO = "ServicioGestor";
	// Almacena el puerto donde escuchará el servicio
	public static final int PUERTO_SERVICIO = 8002;
	// Almacena el puerto del registro RMI por defecto
	public static final int PUERTO_RMI = 1099;
	
	/**
	 * Envia un trino.
	 * @param trino La cadena del trino a enviar
	 * @param nickPropietario El nick propietario del trino
	 * @return El identificador único del trino (timestamp)
	 * @throws RemoteException
	 */
	public long enviarTrino(String trino, String nickPropietario) throws RemoteException;
	
	/**
	 * Agrega el nick de un usuario como seguidor de otro.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @return Número entero que codifica el resultado de la operacion:
	 * 			0 - Éxito
	 * 			1 - Error. El nick a seguir no existe
	 * @throws RemoteException
	 */
	public int seguirA(String nickSeguidor, String nickSeguido) throws RemoteException;
	
	/**
	 * Elimina el nick de un usuario como seguidor de otro.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @return Número entero que codifica el resultado de la operacion:
	 * 			0 - Éxito
	 * 			1 - Error. El nick a seguir no existe
	 * @throws RemoteException
	 */
	public int dejarDeSeguirA(String nickSeguidor, String nickSeguido) throws RemoteException;
		
	/**
	 * Envia los trinos pendientes de recibir a un usuario.
	 * @param nick El nick del usuario con trinos pendientes.
	 * @throws RemoteException
	 */
	public void transmitirTrinos(String nick) throws RemoteException;
	
	
	/**
	 * Elimina un trino de los buffers de usuario.
	 * @param sid Cadena con el identificador único del trino a borrar
	 * @throws RemoteException
	 */
	public void eliminarTrinoBuffers(String sid) throws RemoteException;

}
