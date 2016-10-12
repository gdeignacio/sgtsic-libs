package es.caib.sgtsic.util.bitel;

public final class BitAction
{
  private String name = null;
  private String description = null;
  private String className = null;
  private String path = null;
  private String operation = null;
  private boolean pool = true;
  private String bean = null;
  private boolean validate;
  private String failure = null;
  private boolean history = true;
  private boolean redirect = false;
  private String security;

  public BitAction()
  {
  }

  public Object clone()
  {
    BitAction nueva = new BitAction();
    nueva.setName(this.getName());
    nueva.setClassName(this.getClassName());
    nueva.setPath(this.getPath());
    nueva.setDescription(this.getDescription());
    nueva.setOperation(this.getOperation());
    nueva.setPool(this.isPool());
    nueva.setBean(this.getBean());
    nueva.setValidate(this.isValidate());
    nueva.setFailure(this.getFailure());
    nueva.setHistory(this.isHistory());
    return nueva;
  }


  public final String getName()
  {
    return this.name;
  }

  public final void setName(String s)
  {
    this.name = s;
  }

  public final String getDescription()
  {
    return ((this.description == null? BitCadena.CADENA_VACIA: this.description));
  }

  public final void setDescription(String s)
  {
    this.description = s;
  }

  public final String getOperation()
  {
    return ((this.operation == null? BitCadena.CADENA_VACIA: this.operation));
  }

  public final void setOperation(String s)
  {
    this.operation = s;
  }

  public final String getClassName()
  {
    return className;
  }

  public final void setClassName(String s)
  {
    this.className = s;
  }

  public final String getPath()
  {
    return path;
  }

  public final void setPath(String s)
  {
    this.path = s;
  }

  /**
   * Retorna el valor de la etiqueta <security>. En el caso de que sea null
   * retornar� el valor del primer /path/ contenido en la etiqueta path
   * @return
   */
  public final String getDirectory()
  {
    if (this.getSecurity() != null)
    {
      return this.getSecurity();
    }
    return this.path.substring(0, this.path.lastIndexOf("/") + 1);
  }


  public void setPool(boolean pool)
  {
    this.pool = pool;
  }


  public boolean isPool()
  {
    return pool;
  }


  public void setBean(String bean)
  {
    this.bean = bean;
  }


  public String getBean()
  {
    return bean;
  }


  public void setValidate(boolean validate)
  {
    this.validate = validate;
  }


  public boolean isValidate()
  {
    return validate;
  }


  public void setFailure(String failure)
  {
    this.failure = failure;
  }


  public String getFailure()
  {
    return failure;
  }


  public void setHistory(boolean history)
  {
    this.history = history;
  }


  public boolean isHistory()
  {
    return history;
  }


  public void setRedirect(boolean redirect)
  {
    this.redirect = redirect;
  }


  public boolean isRedirect()
  {
    return redirect;
  }


  public void setSecurity(String security)
  {
    this.security = security;
  }


  public String getSecurity()
  {
    return security;
  }

  public boolean isInsert()
  {
    return getName().endsWith("Ins");
  }

  public boolean isUpdate()
  {
    return getName().endsWith("Set");
  }

  public boolean isPrint()
  {
    return getName().endsWith("Prt");
  }
}
