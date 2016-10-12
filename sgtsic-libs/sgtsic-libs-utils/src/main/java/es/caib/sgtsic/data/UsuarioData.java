package es.caib.sgtsic.data;

import es.caib.sgtsic.util.bitel.BitCadena;

/**
 * La clase <code>UsuarioData</code> contiene la informaci�n de Usuario,
 * seg�n el patr�n de dise�o <a href="http://java.sun.com/products/javabeans/">JavaBeans</a>.
 *
 * @author  Paco Ros (FRO)
 * @version 1.0, 06/10/2008
 */
public class UsuarioData extends DataAdapter
{
  
  /** C�digo interno. */
  private int id;
  
  /** Nombre. */
  private String nombre;
  
  /** Login CAIB. */
  private String login;
  
  /** E.mail. */
  private String email;
  
  /** Activo. */
  private boolean activo;
  
  /** C�digo de isla. */
  private int idIsla;
  private String islaNombre;
  
  
  
  /**
   * Establece el valor del atributo id.
   * 
   * @param    newId el nuevo valor del atributo.
   */
  public final void setId(int newId) {
    this.id = newId;
  }
  
  /**
   * Recupera el valor del atributo id.
   * 
   * @return el valor del atributo.
   */
  public final int getId() {
    return this.id;
  }
  
  
  /**
   * Establece el valor del atributo nombre.
   * 
   * @param    newNombre el nuevo valor del atributo.
   */
  public final void setNombre(String newNombre) {
    this.nombre = newNombre;
  }
  
  /**
   * Recupera el valor del atributo nombre, devolviendo una cadena vac�a si es null.
   * 
   * @return el valor del atributo.
   */
  public final String getNombre() {
    return BitCadena.NVL(this.nombre, BitCadena.CADENA_VACIA);
  }
  
  
  /**
   * Establece el valor del atributo login.
   * 
   * @param    newLogin el nuevo valor del atributo.
   */
  public final void setLogin(String newLogin) {
    this.login = newLogin;
  }
  
  /**
   * Recupera el valor del atributo login, devolviendo una cadena vac�a si es null.
   * 
   * @return el valor del atributo.
   */
  public final String getLogin() {
    return BitCadena.NVL(this.login, BitCadena.CADENA_VACIA);
  }
  
  
  /**
   * Establece el valor del atributo email.
   * 
   * @param    newEmail el nuevo valor del atributo.
   */
  public final void setEmail(String newEmail) {
    this.email = newEmail;
  }
  
  /**
   * Recupera el valor del atributo email, devolviendo una cadena vac�a si es null.
   * 
   * @return el valor del atributo.
   */
  public final String getEmail() {
    return BitCadena.NVL(this.email, BitCadena.CADENA_VACIA);
  }
  
  
  /**
   * Establece el valor del atributo activo.
   * 
   * @param    newActivo el nuevo valor del atributo.
   */
  public final void setActivo(boolean newActivo) {
    this.activo = newActivo;
  }
  
  /**
   * Recupera el valor del atributo activo.
   * 
   * @return el valor del atributo.
   */
  public final boolean isActivo() {
    return this.activo;
  }
  
  
  /**
   * Establece el valor del atributo idIsla.
   * 
   * @param    newIdIsla el nuevo valor del atributo.
   */
  public final void setIdIsla(int newIdIsla) {
    this.idIsla = newIdIsla;
  }
  
  /**
   * Recupera el valor del atributo idIsla.
   * 
   * @return el valor del atributo.
   */
  public final int getIdIsla() {
    return this.idIsla;
  }
  
  
  
  /**
   * Devuelve el c�digo adecuado para una lista de valores.
   * 
   * @return el c�digo para una lista de valores.
   */
  public final String getCodigoLOV() {
    return Integer.toString(this.getId());  // [BitCase: Es posible que este campo no sea el adecuado.]
  }
  
  /**
   * Devuelve el nombre adecuado para una lista de valores.
   * 
   * @return el nombre para una lista de valores.
   */
  public final String getValorLOV() {
    return this.getNombre();  // [BitCase: Es posible que este campo no sea el adecuado.]
  }
  
  
  /**
   * Indica si otro objeto es "igual" a �ste.
   * 
   * @param  obj    el objeto con el que se realiza la comparaci�n.
   * @return <code>true</code> si el objeto es el mismo que el del argumento, <code>false</code> en caso contrario.
   */
  public final boolean equals(Object obj)
  {
    // Declaramos los objetos del m�todo
    boolean esIgual = false;
    UsuarioData usu = null;
    
    // Evaluamos si los objetos son iguales
    if (obj!=null && obj instanceof UsuarioData) {
      usu = (UsuarioData) obj;
      esIgual = true
             && this.getId() == usu.getId()
             && this.getNombre().equals(usu.getNombre())
             && this.getLogin().equals(usu.getLogin())
             && this.getEmail().equals(usu.getEmail())
             && this.isActivo() == usu.isActivo()
             && this.getIdIsla() == usu.getIdIsla();
    }
    
    // Devolvemos el resultado
    return esIgual;
  }
  
  
  /**
   * Recupera el estado del objeto en forma de String.
   *
   * @return una cadena de texto con la informaci�n de los atributos del objeto.
   */
  public final String toString() {
    try {
      return BitCadena.doToString(this);
    }
    catch (Exception e) { return "(UsuarioData) Error en toString()"; }
  }

	public String getIslaNombre() {
		if (islaNombre == null) return BitCadena.CADENA_VACIA;
		else return islaNombre;
	}

	public void setIslaNombre(String islaNombre) {
		this.islaNombre = islaNombre;
	}
  
}
