package es.garridogarcia.uned.sidi.basededatos;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.garridogarcia.uned.sidi.common.CallbackUsuarioInterface;
import es.garridogarcia.uned.sidi.common.Servicio;
import es.garridogarcia.uned.sidi.common.ServicioDatosInterface;
import es.garridogarcia.uned.sidi.common.Trinador;
import es.garridogarcia.uned.sidi.common.Trino;

/**
 * Esta clase implementa la interfaz remota del servicio de datos y extiende la clase
 * Servicio. Se encarga de almacenar todos los datos del sistema para que los clientes
 * del servicio puedan consultar, añadir o eliminar dichos datos.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
@SuppressWarnings("serial")
public class ServicioDatosImpl extends Servicio implements ServicioDatosInterface {
	
	// Lista de usuarios registrados en el sistema
	private List<Trinador> trinadores;
	// Lista de trinos enviados por los usuarios del sistema
	private List<Trino> trinos;
	// Mapa que contiene los usuarios logeados en el sistema con su repectivo servicio de callback
	private Map<String, CallbackUsuarioInterface> callbackPorNickLogeado;
	// Mapa que relaciona los nicks de los usuarios del sistema con los nicks de sus seguidores.
	private Map<String,List<String>> seguidoresPorNick;
	// Mapa que relaciona los nicks de los usuarios del sistema con su buffer de trinos.
	private Map<String,List<Trino>> bufferPorNick;
	
	/**
	 * Constructor.
	 * El constructor de la clase llama al constructor de la superclase, pasándole como parámetros
	 * el nombre del servicio, el puerto rmi del registro y el puerto del servicio, que son declarados
	 * en la interfaz implementada por la clase y crea las estructuras de datos.
	 */
	protected ServicioDatosImpl() throws RemoteException {
		
		super(NOMBRE_SERVICIO, PUERTO_RMI, PUERTO_SERVICIO);
		
		trinadores = new ArrayList<Trinador>();
		trinos = new ArrayList<Trino>();
		callbackPorNickLogeado = new HashMap<String,CallbackUsuarioInterface>();
		seguidoresPorNick = new HashMap<String,List<String>>();
		bufferPorNick = new HashMap<String,List<Trino>>();
	}
	
	/**
	 * Devuelve la lista de trinos del sistema.
	 * @return Lista de trinos
	 */
	public List<Trino> obtenerTrinos() {
		
		return trinos;
	}
	
	/**
	 * Devuelve la lista de nicks logeados en el sistema.
	 * @return Lista de nicks
	 * @throws RemoteException
	 */
	@Override
	public List<String> obtenerNicksLogeados() throws RemoteException {
		
		return new ArrayList<String>(callbackPorNickLogeado.keySet());
	}
	
	/**
	 * Devuelve la lista de usuarios registrados en el sistema.
	 * @return Lista de usuarios
	 * @throws RemoteException
	 */
	@Override
	public List<Trinador> obtenerUsuarios() throws RemoteException {
		
		return trinadores;
	}
	
	/**
	 * Almacena un usuario en el sistema y crea su lista de seguidores
	 * y su buffer de trinos.
	 * @param t Usuario a almacenar
	 * @throws RemoteExcepction
	 */
	@Override
	public void almacenarUsuario (Trinador t) throws RemoteException {
		
		trinadores.add(t);
		seguidoresPorNick.put(t.getNick(), new ArrayList<String>());
		bufferPorNick.put(t.getNick(), new ArrayList<Trino>());		
	}
	
	/**
	 * Almacena el nick de un usuario logeado con su callback.
	 * @param nick El nick del usuario
	 * @param callback Interfaz remota de callback del usuario
	 * @throws RemoteException
	 */
	@Override
	public void almacenarCallbackPorNickLogeado (String nick, CallbackUsuarioInterface callback) throws RemoteException {
		
		callbackPorNickLogeado.put(nick, callback);
	}
	
	/**
	 * Elimina el nick y el callback de un usuario del almacén de usuarios logeados.
	 * @param nick El nick del usuario
	 * @throws RemoteException
	 */
	@Override
	public void eliminarCallbackPorNickLogeado (String nick) throws RemoteException {
		
		callbackPorNickLogeado.remove(nick);
	}
	
	/**
	 * Almacena un trino en el almacén de trinos.
	 * @param t Trino a almacenar
	 * @throws RemoteException
	 */
	@Override
	public void almacenarTrino (Trino t) throws RemoteException {
		
		trinos.add(t);
	}
	
	/**
	 * Almacena un trino en el buffer de un usuario.
	 * @param t Trino a almacenar
	 * @param nick  El nick del usuario
	 * @throws RemoteException
	 */
	@Override
	public void almacenarTrinoBuffer (Trino t, String nick) throws RemoteException {
		
		bufferPorNick.get(nick).add(t);		
	}
	
	/**
	 * Devuelve una lista con los nicks que siguen a un usuario.
	 * @param nick El nick de usuario del que obtener los nicks seguidores
	 * @return Lista de nicks de los seguidores
	 */
	@Override
	public List<String> obtenerSeguidores (String nick) throws RemoteException {
		
		return seguidoresPorNick.get(nick);
	}
	
	/**
	 * Devuelve el mapa que relaciona los nicks de usuario con sus respectivos
	 * nicks seguidores.
	 * @return Mapa que relaciona nicks de usuario con nicks seguidores
	 * @throws RemoteException
	 *//*
	@Override
	public Map<String,List<String>> obtenerSeguidoresPorNick() throws RemoteException {
		
		return seguidoresPorNick;
	}
	*/
	/**
	 * Devuelve el mapa que relaciona los nicks de los usuarios logueados con sus respectivas
	 * interfaces remotas.
	 * @return Mapa que relaciona nicks logeados con sus interfaces remotas
	 * @throws RemoteException
	 */
	@Override
	public Map<String,CallbackUsuarioInterface> obtenerCallbackPorNickLogeado() throws RemoteException {
		
		return callbackPorNickLogeado;
	}
	
	/**
	 * Almacena un nick en la lista de seguidores de otro nick.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick del usuario que es seguido
	 * @throws RemoteException
	 */
	@Override
	public void almacenarSeguidor (String nickSeguidor, String nickSeguido) throws RemoteException {
		
		seguidoresPorNick.get(nickSeguido).add(nickSeguidor);
	}
	
	/**
	 * Elimina un nick de la lista de seguidores de otro nick.
	 * @param nickSeguidor El nick del usuario que sigue
	 * @param nickSeguido El nick de usuario que es seguido
	 * @throws RemoteException
	 */
	@Override
	public void eliminarSeguidor (String nickSeguidor, String nickSeguido) throws RemoteException {
		
		seguidoresPorNick.get(nickSeguido).remove(nickSeguidor);
	}
	
	/**
	 * Devuelve el buffer de trinos de un usuario y lo vacía.
	 * @param nick El nick de usuario del cual queremos recuperar el buffer
	 * @return El buffer del usuario que es una lista de trinos
	 * @throws RemoteException
	 */
	@Override
	public List<Trino> obtenerBufferUsuario (String nick) throws RemoteException {
		
		List<Trino> trinos = new ArrayList<Trino>(bufferPorNick.get(nick));
		
		bufferPorNick.get(nick).clear();
		
		return trinos;
	}
	
	/**
	 * Devuelve la interfaz remota de callback del usuario cuyo nick se pasa como
	 * parámetro.
	 * @param nick El nick del usuario del cual queremos obtener la interfaz remota.
	 * @return La interfaz remota del callback del usuario.
	 * @throws RemoteException
	 */	
	@Override
	public CallbackUsuarioInterface obtenerCallbackUsuario (String nick) {
		
		return callbackPorNickLogeado.get(nick);
	}
	
	/**
	 * Elimina un trino de los buffers de usuario.
	 * @param id El identificador único del trino a borrar
	 * @throws RemoteException
	 */
	@Override
	public void eliminarTrinoBuffers (long id) throws RemoteException {
		
		for (Map.Entry<String, List<Trino>> map :  bufferPorNick.entrySet()) {
			Iterator<Trino> it = map.getValue().iterator();
			while (it.hasNext()) {
				Trino t = it.next();
				if (t.ObtenerTimestamp() == id) {
					it.remove();
				}
			}
		}
	}
}
