package es.caib.sgtsic.data;


import java.io.*;


/**
* Clase que almacena d�as, horas, minutos, segundos y milisegundos de un Timestamp
* @created 16/01/2004
*/
public class TiempoData implements Serializable
{
  private int dias;
  private int horas;
  private long milisegundos;
  private int minutos;
  private int segundos;


  public TiempoData()
  {
  }


  public String toString()
  {
    return "D�as: " + dias + "\nHoras: " + horas + "\nMinutos: " + minutos + "\nSegundos: " + segundos + "\nMilisegundos: " + 
      milisegundos + "\n";
  }


  public int getDias()
  {
    return dias;
  }

  public int getHoras()
  {
    return horas;
  }

  public long getMilisegundos()
  {
    return milisegundos;
  }

  public int getMinutos()
  {
    return minutos;
  }

  public int getSegundos()
  {
    return segundos;
  }


  /**
     * @param    newVal
     */
  public void setDias(int newVal)
  {
    dias = newVal;
  }

  /**
     * @param    newVal
     */
  public void setHoras(int newVal)
  {
    horas = newVal;
  }

  /**
     * @param    newVal
     */
  public void setMilisegundos(long newVal)
  {
    milisegundos = newVal;
  }

  /**
     * @param    newVal
     */
  public void setMinutos(int newVal)
  {
    minutos = newVal;
  }

  /**
     * @param    newVal
     */
  public void setSegundos(int newVal)
  {
    segundos = newVal;
  }
}
