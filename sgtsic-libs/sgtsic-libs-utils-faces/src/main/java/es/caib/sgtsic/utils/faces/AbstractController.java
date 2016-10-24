package es.caib.sgtsic.utils.faces;

//import es.caib.mataderos.back.manager.SesionManager;
//import es.caib.mataderos.back.persistence.utils.AbstractServiceInterface;
//import static es.caib.mataderos.common.definitions.JNDIValues.getFacadeLocalClassName;
import static es.caib.sgtsic.utils.ejb.JNDI.getFacadeLocalClassName;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.sgtsic.utils.ejb.AbstractServiceInterface;
import java.util.Arrays;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.OneToOne;

public abstract class AbstractController<E> {
    
    private final static List<String> VALID_COLUMN_KEYS = new ArrayList<>(); //Arrays.asList("id", "brand", "year", "color", "price");
     
    private String columnTemplate = ""; //id brand year";
     
    private List<ColumnModel> columns;
    
    
    
    

    protected static Log log = LogFactory.getLog(AbstractController.class);
    
    private static final String GETID = "getId";

    private final Class<E> entityClass;
    private AbstractServiceInterface<E> serviceInterfaceClass;

    public AbstractController(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract AbstractServiceInterface<E> getService();
    
    //protected SesionManager sesionManager;

    private List<E> lista;
    private List<E> listaFiltrada;

   
    private E current;
    private boolean editadoOk;
    private String id;
    //private boolean borrable;
    private Map<String, String> confirmMsg;
   
    
    private Map<String, List> listas;
    private Map<String, List> listasDetalle;
    private Object currentDetalle;
    
   
    public Object getCurrentDetalle() {
        return currentDetalle;
    }

    public void setCurrentDetalle(Object currentDetalle) {
        this.currentDetalle = currentDetalle;
    }

    public List getListaDetalle(String key){
        return listasDetalle.get(key);
    }
    
    public Map<String, List> getListasDetalle() {
        return listasDetalle;
    }

    public void setListasDetalle(Map<String, List> listasDetalle) {
        this.listasDetalle = listasDetalle;
    }
    private Map<String, Object> idsListas;

    public Map<String, Object> getIdsListas() {
        return idsListas;
    }

    public void setIdsListas(Map<String, Object> idsListas) {
        this.idsListas = idsListas;
    }

    public Map<String, List> getListas() {
        return listas;
    }

    public void setListas(Map<String, List> listas) {
        this.listas = listas;
    }
    
   

    public Map<String, String> getConfirmMsg() {
        return confirmMsg;
    }

    public void setConfirmMsg(Map<String, String> confirmMsg) {
        this.confirmMsg = confirmMsg;
    }
    
     public String getColumnTemplate() {
        return columnTemplate;
    }

    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public List<E> getListaFiltrada() {
        return listaFiltrada;
    }

    public void setListaFiltrada(List<E> listaFiltrada) {
        this.listaFiltrada = listaFiltrada;
    }
    

    public void inicio() {
        this.editadoOk = false;
        this.current = null;
        this.lista = new ArrayList<>();
        this.listas = new HashMap<>();
        this.listasDetalle = new HashMap<>();
        this.currentDetalle = null;
        this.idsListas = new HashMap<>();
        this.confirmMsg = new HashMap<>();
        //this.borrable = false;
        populateLista();
        populateListas();
        
        createDynamicColumns();
        
    }

    /*
    
      private void createDynamicColumns() {
        String[] columnKeys = columnTemplate.split(" ");
        columns = new ArrayList<>();   
         
        for(String columnKey : columnKeys) {
            String key = columnKey.trim();
             
            if(VALID_COLUMN_KEYS.contains(key)) {
                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
            }
        }
    }
    
    */
    
    
    private void createDynamicColumns() {
        
        for (Field f:this.entityClass.getFields()){
           // if (f.isAnnotationPresent(OneToMany.class)) continue;
           // if (f.isAnnotationPresent(OneToOne.class)) continue;
           // if (f.isAnnotationPresent(ManyToMany.class)) continue;
           // if (f.isAnnotationPresent(ManyToOne.class)) continue;
            
            log.debug(f.getName());
            columns.add(new ColumnModel(f.getName().toUpperCase(), f.getName()));
        }
        
    }
     
    public void updateColumns(String component) {
        
        //":form:cars"
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(component);
        table.setValueExpression("sortBy", null);
         
        //update columns
        createDynamicColumns();
    }
    
    
    public List<E> getLista() {
        return lista;
    }

    public void setLista(List<E> lista) {
        this.lista = lista;
    }

    public E getCurrent() {
        return current;
    }

    public void setCurrent(E current) {
        this.current = current;
    }

    public boolean isEditadoOk() {
        return editadoOk;
    }

    public void setEditadoOk(boolean editadoOk) {
        this.editadoOk = editadoOk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /*
    public void setSesionManager(SesionManager sesionManager){
        this.sesionManager = sesionManager;
    }
    
    public SesionManager getSesionManager(){
        return sesionManager;
    }*/
    
    public abstract void load();
    

    public void populateLista() {
        log.debug("Entramos a lista " + entityClass.getSimpleName());
        lista = getService().findAll();
        log.debug("SIZE LISTA " + entityClass.getSimpleName() + " " + lista.size());
    }

    public void loadEditar() {
        editadoOk = false;
    }

    public void save(String component) {
        log.debug("EDITAR ACTION");
        
        
        
        getService().edit(current);
        populateLista();
        JSFUtil.clearSubmittedValues(component);
        editadoOk = true;
    }

    public void edit(Object key) {
        current = getService().wideFind(key);
        if (current == null) {
            return;
        }
        populateListasDetalle();
        //populateBorrable();
    }
    
    public void narrowEdit(Object key){
        current = getService().find(key);
        if (current == null) {
            return;
        }
    }
    

    public void delete() {
        log.debug("delete " + entityClass.getSimpleName());
        if (current == null) {
            return;
        }
        getService().remove(current);
        populateLista();
        JSFUtil.addMessage(entityClass.getSimpleName(),"Eliminado");
        current = null;
    }

    public void nuevo() throws InstantiationException, IllegalAccessException {
        current = entityClass.newInstance();
        initManyToOne();
        //borrable = false;
    }

    //public void populateBorrable(Object key) {
    //    long clave = (long) key;
    //    borrable = true;
    //    //borrable = (current != null && clave != 0) ? getFacade().borrable(clave) : false;
    //}
    
    
   public void populateConfirmMsg(String header, String message){
       this.confirmMsg.put("HEADER", header);
       this.confirmMsg.put("MESSAGE", message);
   }

   public boolean itemIsBorrable() {
       
       Object key = getCurrentId();
       
       if (key==null) return false;
       
       boolean esBorrable = getService().borrable(key);
       
       log.debug("Es borrable  " + esBorrable);
       
       return esBorrable;
       
   }
    
    private Object getCurrentId(){
        
        if (current==null) return null;
        
        Object key = null;
        
        try {
            Method m = entityClass.getMethod(GETID);
            key = m.invoke(current, (Object[]) null);
            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }

    private AbstractServiceInterface getRelatedFacade(Class relatedEntity) {

        String facadeClassName = getFacadeLocalClassName(relatedEntity);
        AbstractServiceInterface entityFacade = null;

        for (Field mf : this.getClass().getDeclaredFields()) {

            if (facadeClassName.equals(mf.getType().getSimpleName())
                    && mf.isAnnotationPresent(EJB.class)) {
                try {
                    mf.setAccessible(true);
                    entityFacade = (AbstractServiceInterface) mf.get(this);
                    mf.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return entityFacade;
    }

    private void initManyToOne() {
        
        if (current==null) return;
        
        for (Field f : entityClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(ManyToOne.class)) {

                try {
                    f.setAccessible(true);
                    f.set(current, f.getType().newInstance());
                    f.setAccessible(false);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private void populateListas() {

        for (Field f : entityClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(ManyToOne.class)) {
                String key = f.getName();
                AbstractServiceInterface relatedFacade = getRelatedFacade(f.getType());
                log.debug("Entramos a lista " + key);
                listas.put(key, relatedFacade.findAll());
                if (listas.containsKey(key)) {
                    log.debug("SIZE LISTA " + key + " " + listas.get(key).size());
                }
            }
        }

    }

    private void populateListasDetalle() {
        
        if (current==null) return;

        for (Field f : entityClass.getDeclaredFields()) {

            boolean hasToManyAnnotations = (f.isAnnotationPresent(OneToMany.class))
                    || (f.isAnnotationPresent(ManyToMany.class));

            if (hasToManyAnnotations) {
                String key = f.getName();
                List listaDetalle = null;
                log.debug("Entramos a lista " + key);
                try {
                    f.setAccessible(true);
                    listaDetalle = (List) f.get(current);
                    f.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
                }
                listasDetalle.put(key, listaDetalle);
                if (listasDetalle.containsKey(key)) {
                    log.debug("SIZE LISTA " + key + " " + listasDetalle.get(key).size());
                }
            }
        }

    }

}
