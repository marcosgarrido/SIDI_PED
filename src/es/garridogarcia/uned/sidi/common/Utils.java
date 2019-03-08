package es.garridogarcia.uned.sidi.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

public class Utils {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	public static final String HOSTNAME = "java.rmi.server.hostname";
	
	/**
	 * Establece el codebase.
	 * @param c Una clase genérica
	 */
	public static void setCodebase(Class<?> c) {
		
		String ruta = c.getProtectionDomain().getCodeSource()
				      .getLocation().toString();
		
		String path = System.getProperty(CODEBASE);
		
		if (path != null && !path.isEmpty()) {
			ruta = path + " " + ruta;
		}
		
		System.setProperty(CODEBASE, ruta);
	}
	
	/**
	 * Establece el hostname.
	 */
	public static void setHostname() {
		
		System.setProperty(HOSTNAME, getHostAddress());
	}
	
	/**
	 * Arranca un registro RMI.
	 * @param nPuertoRMI El puerto del registro RMI
	 */
	public static void arrancarRegistro (int nPuertoRMI) {
	
		try {
			Registry registro = LocateRegistry.getRegistry(nPuertoRMI);
			registro.list();
		} catch (RemoteException e) {
			try {
				LocateRegistry.createRegistry(nPuertoRMI);
			} catch (RemoteException e1) {
				System.out.println(
						"Error. No se ha podido crear el registro RMI");
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Obtiene una cadena con la ip del host desde donde se llama al
	 * método.
	 * @return Cadena con la ip del host
	 */	
	public static String getHostAddress() {
		
		try {
            Enumeration<NetworkInterface> interfacesRed = NetworkInterface.getNetworkInterfaces();
            while (interfacesRed.hasMoreElements()) {
                NetworkInterface interfazRed = (NetworkInterface) interfacesRed.nextElement();
                Enumeration<InetAddress> direcciones = interfazRed.getInetAddresses();
                while(direcciones.hasMoreElements()) {
                    InetAddress direccion = (InetAddress) direcciones.nextElement();
                    if (!direccion.isLinkLocalAddress() 
                     && !direccion.isLoopbackAddress()
                     && direccion instanceof Inet4Address) {
                        return direccion.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("No ha sido posible obtener la IP local " );
            e.printStackTrace();
        }
        return null;
	}
}
