package es.garridogarcia.uned.sidi.usuario;

import java.rmi.RemoteException;
import java.util.List;

import es.garridogarcia.uned.sidi.common.CallbackUsuarioInterface;
import es.garridogarcia.uned.sidi.common.Servicio;
import es.garridogarcia.uned.sidi.common.Trino;

/**
 * Esta clase implementa la interfaz remota de callback de usuario y extiende la clase 
 * Servicio. Se encarga de que el usuario pueda recibir trinos nuevos del servidor, sin
 * sin tener que estar consultando al servidor constantemente.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
@SuppressWarnings("serial")
public class CallbackUsuarioImpl extends Servicio implements CallbackUsuarioInterface {
	
	/**
	 * Constructor.
	 * El constructor de la clase llama al constructor de la superclase, pasándole como parámetros
	 * el nombre del servicio, el puerto rmi del registro y el puerto del servicio, que son declarados
	 * en la interfaz implementada por la clase y crea las estructuras de datos.
	 */
	protected CallbackUsuarioImpl(String identificadorUnico) throws RemoteException {
		
		super(NOMBRE_SERVICIO, PUERTO_RMI, identificadorUnico);
	}
	
	/**
	 * Imprime por pantalla un trino.
	 * @param t El trino a imprimir
	 * @throws RemoteException
	 */
	public void imprimirTrino (Trino t) throws RemoteException {
		
		System.out.printf("> %s # %s\n", t.ObtenerNickPropietario(), t.ObtenerTrino());
	}
	
	/**
	 * Imprime por pantalla una lista de trinos.
	 * @param trinos La lista de trinos a imprimir
	 * @throws RemoteException
	 */
	public void imprimirTrinos (List<Trino> trinos) throws RemoteException {
		
		for (Trino t : trinos) {
			System.out.printf("> %s # %s\n", t.ObtenerNickPropietario(), t.ObtenerTrino());
		}
	}
}