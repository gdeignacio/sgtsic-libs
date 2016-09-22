/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.utils.generacion;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.WordUtils;

/**
 * Contenedor de datos para la generación de documentos
 * a partir de plantillas.
 *
 * Contiene la colección de parámetros, resultado de condiciones,
 * objetos y colecciones de datos para el rellenado de plantillas.
 * @author ABV
 */
public class GeneracionDocumentoData {

    private Map<String, Boolean> condiciones;
    private Map<String, String> parametros;
    private Map<String, Object> objetos;
    private Map<String, List<GeneracionDocumentoData>> colecciones;
    private GeneracionDocumentoData superior;

    /**
     * Genera un nuevo contenedor sin datos iniciales.
     */
    public GeneracionDocumentoData() {
        this.condiciones = new HashMap<String, Boolean>();
        this.parametros = new HashMap<String, String>();
        this.objetos = new HashMap<String, Object>();
        this.colecciones = new HashMap<String, List<GeneracionDocumentoData>>();
        this.superior = null;
    }

    /**
     * Añade la condición al contenedor de datos.
     * @param nombre Nombre de la condición
     * @param valor Evaluación de la condición
     */
    public void addCondicion(String nombre, Boolean valor) {
        this.condiciones.put(nombre, valor);
    }

    /**
     * Añade un parámetro al contenedor de datos.
     * @param nombre Nombre del parámetro
     * @param valor Valor del parámetro
     */
    public void addParametro(String nombre, String valor) {
        this.parametros.put(nombre, valor);
    }

    /**
     * Añade un objeto al contenedor de datos.
     * @param nombre Nombre del objeto
     * @param objeto Objeto
     */
    public void addObjeto(String nombre, Object objeto) {
        this.objetos.put(nombre, objeto);
    }

    /**
     * Añade toda una colección al contenedor de datos.
     * @param nombre Nombre de la colección
     * @param coleccion Listado de subcontenedores
     */
    public void addColeccion(String nombre, List<GeneracionDocumentoData> coleccion) {
        for (GeneracionDocumentoData inferior : coleccion) {
            inferior.setSuperior(this);
        }
        this.colecciones.put(nombre, coleccion);
    }

    /**
     * Añade los elementos del listado a una colección existente.
     * @param nombre Nombre de la colección (si no existe, se crea vacía)
     * @param coleccion Listado de subcontenedores
     */
    public void addToColeccion(String nombre, List<GeneracionDocumentoData> coleccion) {
        for (GeneracionDocumentoData inferior : coleccion) {
            inferior.setSuperior(this);
        }
        if (this.colecciones.get(nombre) == null) {
            this.colecciones.put(nombre, new ArrayList<GeneracionDocumentoData>());
        }
        this.colecciones.get(nombre).addAll(coleccion);
    }

    /**
     * Añade un nuevo elemento a una colección existente.
     * @param nombre Nombre de la colección (si no existe, se crea vacía)
     * @param data Nuevo subcontenedor
     */
    public void addToColeccion(String nombre, GeneracionDocumentoData data) {
        data.setSuperior(this);
        if (this.colecciones.get(nombre) == null) {
            this.colecciones.put(nombre, new ArrayList<GeneracionDocumentoData>());
        }
        this.colecciones.get(nombre).add(data);
    }

    /**
     * Recupera un parámetro del contenedor.
     * @param nombre Nombre del parámetro
     * @return Valor del parámetro (null si no existe)
     */
    public String getParametro(String nombre) {
        return this.parametros.get(nombre);
    }

    /**
     * Determina si existe una condición con el nombre indicado.
     * @param nombre Nombre de la condición.
     * @return La condición existe (no confundir con el resultado de su evaluación).
     */
    public boolean existsCondicion(String nombre) {
        return this.condiciones.get(nombre) != null ? true : false;
    }

    /**
     * Recupera una condición del contenedor.
     * @param nombre Nombre de la condición
     * @return Evaluación de la condición (false si no existe)
     */
    public boolean getCondicion(String nombre) {
        return this.condiciones.get(nombre) != null ? this.condiciones.get(nombre).booleanValue() : false;
    }

    /**
     * Recupera un objeto del contenedor.
     * @param nombre NOmbre del objeto
     * @return El objeto (null si no existe)
     */
    private Object getObjeto(String nombre) {
        return this.objetos.get(nombre);
    }

    /**
     * Recupera una colección del contenedor.
     * @param nombre Nombre de la colección
     * @return Listado de subcontenedores (vacío si no existe)
     */
    public List<GeneracionDocumentoData> getColeccion(String nombre) {
        return this.colecciones.get(nombre) != null ? this.colecciones.get(nombre) : new ArrayList<GeneracionDocumentoData>();
    }

    /**
     * Recupera el contenedor superior en el que está incluído
     * el contenedor actual como parte de una colección.
     * @return El contenedor de nivel superior (null si no existe).
     */
    public GeneracionDocumentoData getSuperior() {
        return superior;
    }

    /**
     * Configura el contenedor superior en el que está incluído el actual.
     * Se cumplirá que this.superior.getColeccion("...") incluye al contenedor actual.
     * @param superior Contenedor de datos superior.
     */
    public void setSuperior(GeneracionDocumentoData superior) {
        this.superior = superior;
    }

    /**
     * Recupera la propiedad indicada para el objeto con el nombre especificado.
     * @param nombre Nombre del objeto (puede ser null)
     * @param propiedad Nombre del atributo del objeto (incluyendo varios niveles separados por punto)
     * @return Valor de la propiedad (null si no es accesible o no existe)
     */
    public Object getPropiedadObjeto(String nombre, String propiedad) {
        Object ret = null;
        
        try {
            ret = this.objetos.get(nombre) != null ? this.getPropiedadObjetoRec(this.objetos.get(nombre), propiedad) : null;
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GeneracionDocumentoData.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return ret;
    }

    /**
     * Recupera la propiedad indicada para el objeto recibido.
     * Este método se ejecuta recursivamente si la propiedad es de varios niveles (separados por punto)
     * @param objeto El objeto al que acceder
     * @param propiedad Nombre de la propiedad
     * @return Valor de la propiedad (null si no es accesible o no existe)
     * @throws java.lang.NoSuchMethodException No existe un método accesor para la propiedad indicada.
     * @throws java.lang.IllegalAccessException No existe un método accesor público para la propiedad indicada.
     * @throws java.lang.IllegalArgumentException No existe un método accesor sin parámetros para la propiedad indicada.
     * @throws java.lang.reflect.InvocationTargetException Error accediendo al método accesor para el objeto recibido.
     */
    private Object getPropiedadObjetoRec(Object objeto, String propiedad) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (objeto == null) {
            return null;
        }

        Method metodo = null;
        Object ret = null;
        int punto = propiedad.indexOf(".");

        if (punto < 0) {
            // Es un atributo del objeto recibido
            metodo = objeto.getClass().getDeclaredMethod("get" + WordUtils.capitalize(propiedad));
            ret = metodo.invoke(objeto);
        } else {
            // Es un atributo de un objeto anidado, llamamos recursivamente
            String propiedadRec;
            Object objetoRec;
            propiedadRec = propiedad.substring(punto + 1);
            propiedad = propiedad.substring(0, punto);
            metodo = objeto.getClass().getDeclaredMethod("get" + WordUtils.capitalize(propiedad));
            objetoRec = metodo.invoke(objeto);
            ret = this.getPropiedadObjetoRec(objetoRec, propiedadRec);
        }
        return ret;
    }


    /**
     * Transforma un contenedor de datos a tipos básicos compatibles
     * con la librería JODReports (Map, List i JavaBeans).
     * @return Contenedor de datos con tipos básicos.
     */
    public Map<String, Object> toMap() {
        List<GeneracionDocumentoData> listGen = null;
        List<Map<String, Object>> list = null;
        Map<String, Object> map = new HashMap<String, Object>();

        // Pasamos las propiedades simples
        for(String param: this.parametros.keySet()) {
            map.put(param, this.parametros.get(param));
        }

        // Pasamos las condiciones (son como cualquier propiedad)
        for(String cond: this.condiciones.keySet()) {
            map.put(cond, this.condiciones.get(cond));
        }

        // Pasamos los objetos (son como cualquier propiedad, la librería reports sabe acceder a ellos)
        for(String obj: this.objetos.keySet()) {
            map.put(obj, this.objetos.get(obj));
        }

        // Pasamos las colecciones como listas Java
        for(String col: this.colecciones.keySet()) {
            listGen = this.colecciones.get(col);
            list = new ArrayList<Map<String, Object>>();
            for(GeneracionDocumentoData gen: listGen) {
                list.add(gen.toMap());
            }
            map.put(col, list);
        }

        return map;
    }
}
