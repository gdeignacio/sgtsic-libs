
/* Static Model */
package es.caib.sgtsic.data;

public class AttachmentData extends DataAdapter
{
  private boolean inline = false;
  private byte[] bytes;
  private String content;
  private String nombre;


  public AttachmentData(byte[] b)
  {
    this.bytes = b;
  }

  public AttachmentData(int i)
  {
    this.bytes = new byte[i];
  }


  public String toString()
  {
    return "Nombre: " + nombre + "\nContent: " + content + "\nInline: " + inline + "\nBytes: " + bytes + "\n";
  }


  public byte[] getBytes()
  {
    return this.bytes;
  }

  public String getCodigoLOV()
  {
    return nombre;
  }

  public String getContentType()
  {
    return this.content;
  }

  public String getNombre()
  {
    return this.nombre;
  }

  public int getSize()
  {
    return bytes.length;
  }

  public String getValorLOV()
  {
    return content;
  }

  public boolean isInline()
  {
    return inline;
  }

  public void setBytes(byte[] b)
  {
    this.bytes = b;
  }

  public void setContentType(String s)
  {
    this.content = s;
  }

  public void setInline(boolean b)
  {
    inline = b;
  }

  public void setNombre(String s)
  {
    this.nombre = s;
  }
}
