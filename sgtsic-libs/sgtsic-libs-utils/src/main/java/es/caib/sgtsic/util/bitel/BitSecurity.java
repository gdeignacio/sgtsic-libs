package es.caib.sgtsic.util.bitel;

import java.util.LinkedList;
import java.util.ListIterator;

/*
 * Clase que representa los roles que tienen acceso a un cierto directorio
 * Si un directorio es accesible desde todos los roles de la aplicacion lo identificaremos con
 * el rol PUBLIC
 */
public class BitSecurity
{
  private String name = null; // Nombre del directorio sobre el cual aplicamos la seguridad /nombre/
  private LinkedList listaRoles = new LinkedList();


  public BitSecurity()
  {
  }


  public final void addRole(String rol)
  {
    this.listaRoles.add(rol);
  }

  public final String printListaRoles()
  {
    ListIterator it = this.listaRoles.listIterator(0);
    String rol = "";

    while (it.hasNext())
    {
      rol += (String) it.next() + ",";
    }

    return rol;
  }

  public final LinkedList getListaRoles()
  {
    return this.listaRoles;
  }

  public final String getName()
  {
    return this.name;
  }

  public final boolean isPublic()
  {
    return "PUBLIC".equals((String) this.listaRoles.get(0));
  }

  public final void setListaRoles(LinkedList list)
  {
    listaRoles = (LinkedList) list.clone();
  }

  public final void setName(String s)
  {
    this.name = s;
  }
}