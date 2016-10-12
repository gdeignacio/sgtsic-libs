package es.caib.sgtsic.util.bitel;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import es.caib.sgtsic.error.bitel.ErrorControladorResponseNoDefinido;


/**
 * Representa una url a la que podemos redireccionar la petici�n.
 * @author ELU
 * @version 1.0
 * @created 01-sep-2004
 */
public final class BitForward
{
  /**
   * URL a la que podemos redireccionar la peticion
   */
  String url;

  /**
   * Objeto HttpServerResponse para poder realiazar un sendRedirect
   */
  HttpServletResponse response;

  /**
   * Objeto HttpServerRequest para poder realiazar un sendRedirect
   */
  HttpServletRequest request;

  /**
   * Configuraci�n del Servlet
   */
  ServletConfig config;

  /**
   * Contexto de la aplicaci�n por ejemplo /bittemplate
   */
  String context;

  /**
   * Si el valor es true entonces haremos forward con sendRedirect. En caso
   * de que sea false haremos foward con RequestDispatcher.forward
   */
  boolean redirect = false;

  /**
   * Constructor vacio
   */
  public BitForward()
  {

  }

  /**
   * Constructor que mapea la propiedad url
   * @param url String
   */
  public BitForward(String url)
  {
    this.setUrl(url);
  }

  /**
   * Constructor que mapea la propiedad url y la propiedad response
   * @param url String
   * @param response HttpServeletResponse
   */
  public BitForward(String url, HttpServletResponse response)
  {
    this.setUrl(url);
    this.setResponse(response);
  }

  /**
   * Constructor que mapea la propiedad url,la propiedad response y la
   * propiedad context
   * @param url String
   * @param response HttpServeletResponse
   * @param context String
   */
  public BitForward(String url, HttpServletResponse response, String context)
  {
    this.setUrl(url);
    this.setResponse(response);
    this.setContext(context);
  }

  /**
   * Constructor que mapea la propiedad response y la propiedad context
   * @param response HttpServeletResponse
   * @param context String
   */
  public BitForward(HttpServletResponse response, String context)
  {
    this.setResponse(response);
    this.setContext(context);
  }

  /**
   * Mapea la propiededad url
   * @param url String
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  /**
   * Actualiza la propiedad url como bitUrl.getHref()+bitUrl.getParametros()
   * @param bitUrl objeto BitURL
   */
  public void setUrl(BitURL bitUrl)
  {
    if (bitUrl != null)
      this.url = bitUrl.getHref() + bitUrl.getParametros();
  }

  /**
   * Retorna la propiedad url
   * @return url String
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * Mapea la propiedad context
   * @param context String
   */
  public void setContext(String context)
  {
    this.context = context;
  }

  /**
   * Retorna la propiedad context
   * @return context String
   */
  public String getContext()
  {
    return context;
  }

  /**
   * Retorna la url pero mapeando el contexto. Es decir context+url. En el
   * caso de que context sea null, entonces retorna solo la propiedad url
   * @return context+url String
   */
  public String getContextUrl()
  {
    if (getContext() != null)
      return this.getContext() + getUrl();
    return getUrl();
  }

  /**
   * Mapea la propiedad response
   * @param response HttpServletResponse
   */
  public void setResponse(HttpServletResponse response)
  {
    this.response = response;
  }

  /**
   *Retorna la propiedad response
   * @return response HttpServletResponse
   */
  public HttpServletResponse getResponse()
  {
    return response;
  }

  public void setRequest(HttpServletRequest request)
  {
    this.request = request;
  }


  public HttpServletRequest getRequest()
  {
    return request;
  }


  public void setConfig(ServletConfig config)
  {
    this.config = config;
  }


  public ServletConfig getConfig()
  {
    return config;
  }

  public void setRedirect(boolean redirect)
  {
    this.redirect = redirect;
  }


  public boolean isRedirect()
  {
    return redirect;
  }

  /**
   * A�ade una par�metro a la propiedad url. Si la url ya tiene par�metros
   * entonces concatena el s�mbolo & seguido del par�metro. Si no tiene par�metros
   * entonces concatena el s�mbolo ? seguido del par�metro. El par�metro se concatena
   * como nombre = valor, aplicando un URLEncoder al valor. La codificacion aplicada es
   * la especificada en la variable de entorno del BitController. Si no estuviera especificada
   * se aplica ISO-8859-15
   * @param nombre Nombre del par�metro que se quiere concatenar
   * @param valor Valor del par�metro
   */
  public void addParameter(String nombre, String valor) throws UnsupportedEncodingException
  {
    try
    {
      String encoding = config.getServletContext().getInitParameter("encoding");
      if (encoding == null)
        encoding = "ISO-8859-15";
      if (valor != null)
      {
        if (getUrl() != null)
        {
          if (url.indexOf('?') == -1)
            setUrl(url + "?" + nombre + "=" + URLEncoder.encode(valor, encoding));
          else
            setUrl(url + "&" + nombre + "=" + URLEncoder.encode(valor, encoding));
        }
      }
    }
    catch (UnsupportedEncodingException e)
    {
      BitLog.fatal("Error al incorporar el parametro:" + nombre + " con valor:" + valor);
      BitLog.fatal(e.toString());
    }
  }

  /**
   * A�ade una par�metro a la propiedad url. Si la url ya tiene par�metros
   * entonces concatena el s�mbolo & seguido del par�metro. Si no tiene par�metros
   * entonces concatena el s�mbolo ? seguido del par�metro. El par�metro se concatena
   * como nombre = valor, aplicando un URLEncoder al valor
   * @param nombre Nombre del par�metro que se quiere concatenar
   * @param valor Valor del par�metro
   * @param encoding codificacion para el parametro
   */
  public void addParameter(String nombre, String valor, String encoding)
  {
    try
    {
      if (valor != null)
      {
        if (getUrl() != null)
        {
          if (url.indexOf('?') == -1)
            setUrl(url + "?" + nombre + "=" + URLEncoder.encode(valor, encoding));
          else
            setUrl(url + "&" + nombre + "=" + URLEncoder.encode(valor, encoding));
        }
      }
    }
    catch (UnsupportedEncodingException e)
    {
      BitLog.fatal("Error al incorporar el parametro:" + nombre + " con valor:" + valor);
      BitLog.fatal(e.toString());
    }
  }

  /**
   * Concatena los par�metros a la url. El m�todo a�ade el s�mbolo ?
   * @param parametros Par�metros que se quieren incluir
   */
  public void addParameters(String parametros)
  {
    if (parametros != null)
    {
      if (getUrl() != null)
        setUrl(url + "?" + parametros);
    }
  }

  /**
   * Realiza un sendRedirect utilizando la propiedad response a la url indicada
   * en la propiedad url. El m�todo realiza un encodeRedirectURL
   * @throws IOException, ServletException, ErrorControladorResponseNoDefinido
   */
  public void goUrl() throws IOException, ServletException, ErrorControladorResponseNoDefinido
  {
    if (response == null)
      throw new ErrorControladorResponseNoDefinido();
    if (this.redirect)
    {
      response.sendRedirect(response.encodeRedirectURL(getUrl()));
    }
    else
    {
      RequestDispatcher dispatcher = config.getServletContext().getRequestDispatcher(getUrl());
      if (dispatcher != null)
        dispatcher.forward(request, response);
    }
  }

  /**
   * Realiza un sendRedirect utilizando la propiedad response a la url indicada
   * en la propiedad context+url. El m�todo realiza un encodeRedirectURL
   * @throws IOException, ServletException, ErrorControladorResponseNoDefinido
   */
  public void goContextUrl() throws IOException, ServletException, ErrorControladorResponseNoDefinido
  {
    if (response == null)
      throw new ErrorControladorResponseNoDefinido();
    if (this.redirect)
    {
      response.sendRedirect(response.encodeRedirectURL(getContextUrl()));
    }
    else
    {
      RequestDispatcher dispatcher = config.getServletContext().getRequestDispatcher(getUrl());
      if (dispatcher != null)
        dispatcher.forward(request, response);
    }
  }


}
