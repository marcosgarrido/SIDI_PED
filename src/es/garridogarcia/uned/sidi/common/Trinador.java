package es.garridogarcia.uned.sidi.common;

import java.io.Serializable;

/**
 * Esta clase define el tipo de dato Trinador, que utilizamos para almacenar
 * los datos del usuario.
 * @author Marcos Garrido García
 * email: mgarrido376@alumno.uned.es
 */
public class Trinador implements Serializable {
	
	private static final long serialVersionUID = 3571970099026891474L;
	// Almacena el nombre del usuario
	private String nombre;
	// Almacena el nick del usuario
	private String nick;
	// Almacena la contraseña del usuario
	private String password;
	
	/**
	 * Constructor.
	 * @param nombre El nombre del usuario
	 * @param nick El nick del usuario
	 * @param password La contraseña del usuario
	 */
	public Trinador(String nombre, String nick, String password) {
		
		this.setNombre(nombre);
		this.setNick(nick);
		this.setPassword(password);
	}
	
	/**
	 * Devuelve el nombre del usuario.
	 * @return El nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Establece el nombre del usuario.
	 * @param nombre El nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve el nick del usuario.
	 * @return El nick del usuario
	 */
	public String getNick() {
		return nick;
	}
	
	/**
	 * Establece el nick del usuario.
	 * @param nick El nick del usuario
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Devuelve la contraseña del usuario.
	 * @return La contraseña del usuario
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Establece la contraseña del usuario.
	 * @param password La contraseña del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Devuelve una cadena de texto que representa al objeto.
	 * @return Cadena que representa al objeto
	 */
	@Override
	public String toString() {
		
		return (getClass().getName()+"@"+ nombre + "|" + nick + "|" + password);
	}
	
	/**
	 * Establece los criterios con los que considerar si dos objetos.
	 * de la clase son iguales
	 * @param o Objeto a comparar
	 */
	@Override
	public boolean equals (Object o) {
		
		if (o == this) return true;
		if (o == null) return false;
		
		if (!(o instanceof Trinador)) {
			return false;
		} else {
			Trinador t = (Trinador) o;
			return this.getNick().toLowerCase().equals(t.getNick().toLowerCase());
		}
	}
}
