package es.caib.sgtsic.data;




//~--- JDK imports ------------------------------------------------------------

import java.io.*;

/**
 * Mantiene los datos que el usuario ha utilizado para entrar en una aplicaci�n protegida
 *
 * @param usuario "username", "login" o código de usuario utilizado para entrar en la aplicación
 * @param contrasenya "password", credencial o contrasenya que ha utilizado el usuario para entrar en
 *                    la aplicación
 * @author Paco Ros
 * @date 29/07/2004
 **/
public class AutenticacionData extends DataAdapter implements Serializable
{
  private String usuario =  "";
  private String contrasenya = "";

  public String getCodigoLOV()
  {
    return usuario;
  }

  public String getConstrasenya()
  {
    return (contrasenya != null)? contrasenya: "";
  }

  public String getUsuario()
  {
    return (usuario != null)? usuario: "";
  }

  // Funci�n que devuelve la descripci�n que deseamos que aprarezca en las
  // listas de valores

  public String getValorLOV()
  {
    return usuario;
  }

  public void setContrasenya(String contrasenya)
  {
    this.contrasenya = contrasenya;
  }

  public void setUsuario(String usuario)
  {
    this.usuario = usuario;
  }
}
