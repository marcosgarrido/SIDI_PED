package es.garridogarcia.uned.sidi.servidor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import es.garridogarcia.uned.sidi.common.CallbackUsuarioInterface;
import es.garridogarcia.uned.sidi.common.Servicio;
import es.garridogarcia.uned.sidi.common.ServicioDatosInterface;
import es.garridogarcia.uned.sidi.common.ServicioGestorInterface;
import es.garridogarcia.uned.sidi.common.Trino;

/**
 * Esta clase implementa la interfaz remota del servicio gestor y extiende la clase 
 * Servicio. Se encarga de gestionar el envío de trinos y el seguimiento por parte de
 * los usuarios del sistema.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
@SuppressWarnings("serial")
public class ServicioGestorImpl extends Servicio implements ServicioGestorInterface {
	
	// Almacena la interfaz remota del servicio de datos
	private ServicioDatosInterface servDatos;
	
	/**
	 * Constructor.
	 * El constructor de la clase llama al constructor de la superclase, pasándole como parámetros
	 * el nombre del servicio, el puerto rmi del registro y el puerto del servicio, que son declarados
	 * en la interfaz implementada por la clase.
	 */
	protected ServicioGestorImpl(ServicioDatosInterface servDatos) throws RemoteException {
		
		super(NOMBRE_SERVICIO, PUERTO_RMI, PUERTO_SERVICIO);
		this.servDatos = servDatos;
	}
	
	/**
	 * Envia un trino.
	 * @param trino La cadena del trino a enviar
	 * @param nickPropietario El nick propietario del trino
	 * @return El identificador único del trino (timestamp)
	 * @throws RemoteException
	 */
	@Override
	public long enviarTrino (String trino, String nickPropietario) throws RemoteException {
		
		List<String> seguidores = servDatos.obtenerSeguidores(nickPropietario);
		Map<String,CallbackUsuarioInterface> callbackPorNickLogeado = servDatos.obtenerCallbackPorNickLogeado();
		Trino t = new Trino(trino,nickPropietario);
		
		servDatos.almacenarTrino(t);
		
		for (String seguidor : seguidores) {
			if (callbackPorNickLogeado.containsKey(seguidor)) {
				callbackPorNickLogeado.get(seguidor).imprimirTrino(t);
			} else {
				servDatos.almacenarTrinoBuffer(t, seguidor);
			}
		}
		return t.ObtenerTimestamp();
	}
	
	/**
	 * Agrega el nick de un usuario como seguidor de otro.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @return Número entero que codifica el resultado de la operacion:
	 * 			0 - Éxito
	 * 			1 - Error. El nick a seguir no existe
	 * 	        2 - Error. El nick seguidor y el seguido son iguales
	 * @throws RemoteException
	 */
	@Override
	public int seguirA (String nickSeguidor, String nickSeguido) throws RemoteException {
		
		if (nickSeguidor.equals(nickSeguido)) return 2;
		
		List<String> seguidores = servDatos.obtenerSeguidores(nickSeguido);
		
		if (seguidores == null) return 1;
		
		if (!seguidores.contains(nickSeguidor)) {
			servDatos.almacenarSeguidor(nickSeguidor, nickSeguido);
		}
		return 0;
	}
	
	/**
	 * Elimina el nick de un usuario como seguidor de otro.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @return Número entero que codifica el resultado de la operacion:
	 * 			0 - Éxito
	 * 			1 - Error. El nick a dejar de seguir no existe
	 *          2 - Error. El nick seguidor y el seguido son iguales
	 * @throws RemoteException
	 */
	@Override
	public int dejarDeSeguirA (String nickSeguidor, String nickSeguido) throws RemoteException {
		
		if (nickSeguidor.equals(nickSeguido)) return 2;
		
		List<String> seguidores = servDatos.obtenerSeguidores(nickSeguido);
		
		if (seguidores == null) return 1;
		
		if (seguidores.contains(nickSeguidor)) {
			servDatos.eliminarSeguidor(nickSeguidor, nickSeguido);
		}	
		return 0;
	}
	
	/**
	 * Envia los trinos pendientes de recibir a un usuario.
	 * @param nick El nick del usuario con trinos pendientes.
	 * @throws RemoteException
	 */
	@Override
	public void transmitirTrinos (String nick) throws RemoteException {
		
		CallbackUsuarioInterface callback = servDatos.obtenerCallbackUsuario(nick);
		List<Trino> trinos = servDatos.obtenerBufferUsuario(nick);
		
		if (!trinos.isEmpty()) callback.imprimirTrinos(trinos);
	}
	
	/**
	 * Elimina un trino de los buffers de usuario.
	 * @param sid Cadena con el identificador único del trino a borrar
	 * @throws RemoteException
	 */
	@Override
	public void eliminarTrinoBuffers (String sid) throws RemoteException {
		
		servDatos.eliminarTrinoBuffers(Long.parseLong(sid));
	}
}
