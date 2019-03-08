package es.garridogarcia.uned.sidi.common;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.garridogarcia.uned.sidi.common.Utils;

/**
 * Clase que sirve para englobar los servicios, dotándolos de unos atributos y 
 * métodos específicos, extiende la clase UnicastRemoteObject lo que permite que 
 * los servicios que extiendan esta clase puedan generar objetos remotos.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
@SuppressWarnings("serial")
public class Servicio extends UnicastRemoteObject {
	
	// Almacena la URL del objeto remoto
	private String URLRegistro;
	// Almacena el puerto RMI del registro 
	private int puertoRMI;
	
	/**
	 * Constructor.
	 * @param nombreServicio El nombre del servicio
	 * @param puertoRMI Puerto del registro RMI
	 * @param identificadorUnico String con el identificador único del servicio
	 * @throws RemoteException
	 */
	protected Servicio (String nombreServicio, int puertoRMI, String identificadorUnico) throws RemoteException {
		
		super();
		
		URLRegistro = "rmi://" + Utils.getHostAddress() + ":" + puertoRMI
						+ "/" + nombreServicio + "/" + identificadorUnico;
		
		this.puertoRMI = puertoRMI;
	}
	
	/**
	 * Constructor.
	 * @param nombreServicio El nombre del servicio
	 * @param puertoRMI Puerto del registro RMI
	 * @param puertoServicio Puerto donde escucha el servicio
	 * @throws RemoteException
	 */
	protected Servicio(String nombreServicio, int puertoRMI, int puertoServicio) throws RemoteException {
		
		super(puertoServicio);
		
		URLRegistro = "rmi://" + Utils.getHostAddress() + ":" + puertoRMI
						+ "/" + nombreServicio + "/";
		
		this.puertoRMI = puertoRMI;
	}
	
	/**
	 * Establece la URL del objeto remoto.
	 * @param URLRegistro La URL del objeto remoto
	 */
	public void setURLRegistro (String URLRegistro) {
		
		this.URLRegistro = URLRegistro;
	}
	
	/**
	 * Devuelve la URL del objeto remoto.
	 * @return La URL del objeto remoto
	 */
	public String obtenerURLRegistro() {
		
		return URLRegistro;
	}
	
	/**
	 * Establece el puerto del registro RMI.
	 * @param puertoRMI Puerto del registro RMI
	 */
	public void setPuertoRMI (int puertoRMI) {
		
		this.puertoRMI = puertoRMI;
	}
	
	/**
	 * Devuelve el puerto del registro RMI.
	 * @return El puerto del registro RMI
	 */
	public int obtenerPuertoRMI() {
		
		return puertoRMI;
	}
	
	/**
	 * Enlaza el servicio en el registro RMI
	 */
	public void enlazarServicio() {
		
		Utils.arrancarRegistro(puertoRMI);
		
		try {
			Naming.rebind(URLRegistro, this);
		} catch (RemoteException e) {
			System.out.println(
					"Error. Excepción remota.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println(
					"Error. La URL no está bien formateada.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Detiene el servicio, quitando el enlace del servicio con el
	 * registro RMI.
	 */
	public void desenlazarServicio() {
		
		try {
			Naming.unbind(URLRegistro);
		} catch (RemoteException e) {
			System.out.println(
					"Error. Excepción remota.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println(
					"Error. La URL está bien formateada.");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println(
					"Error. La URL está asociada al registro RMI");
			e.printStackTrace();
		}
	}
	
	/**
	 * Detiene el servicio, quitando el enlace del servicio con el
	 * registro RMI.
	 */
	public void eliminarServicio() {
		
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			System.out.println(
					"Error. No existe el objeto remoto del servicio.");
			e.printStackTrace();
		}
	}
}
