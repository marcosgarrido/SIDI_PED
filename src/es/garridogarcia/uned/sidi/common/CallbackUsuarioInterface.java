package es.garridogarcia.uned.sidi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota del servicio de callback de usuario.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public interface CallbackUsuarioInterface extends Remote {
	
	// Almacena el nombre del servicio
	public static final String NOMBRE_SERVICIO = "CallbackUsuario";
	// Almacena el puerto del registro RMI por defecto
	public static final int PUERTO_RMI = 1099;
	
	/**
	 * Imprime por pantalla un trino.
	 * @param t El trino a imprimir
	 * @throws RemoteException
	 */
	public void imprimirTrino(Trino t) throws RemoteException;
	
	/**
	 * Imprime por pantalla una lista de trinos.
	 * @param trinos La lista de trinos a imprimir
	 * @throws RemoteException
	 */
	public void imprimirTrinos (List<Trino> trinos) throws RemoteException;
}
