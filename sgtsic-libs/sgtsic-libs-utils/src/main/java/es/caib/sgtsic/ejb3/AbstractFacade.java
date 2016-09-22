/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.ejb3;



import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gdeignacio
 * @param <T>
 */
public abstract class AbstractFacade<T> {
    
    protected static Log log = LogFactory.getLog(AbstractFacade.class);
    
    private final Class<T> entityClass;
    
    
    /**
     *
     * @param entityClass
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
    
    

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        
        log.debug("Edit                        "   + getEntityManager() );
        log.debug("Edit                        "   + entity.toString() );
        
        getEntityManager().merge(entity);
        
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        return getEntityManager().createQuery("select object(o) from " + entityClass.getSimpleName() + " as o").getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.Query q = getEntityManager().createQuery("select object(o) from " + entityClass.getSimpleName() + " as o");
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) getEntityManager().createQuery("select count(o) from " + entityClass.getSimpleName() + " as o").getSingleResult()).intValue();
    }
    
    private int childrenCount(Object id, Class entityChild, Class mappedBy) {

        log.debug("Entramos a childrenCount");
        log.debug("Entramos a childrenCount" + entityChild);
        log.debug("Entramos a childrenCount" + mappedBy);
        
        String namedQueryName = entityChild.getSimpleName() + ".findCountBy" + entityClass.getSimpleName();

        int count = ((Long) getEntityManager().createNamedQuery(namedQueryName)
                .setParameter("id" + mappedBy.getSimpleName(), id)
                .getSingleResult()).intValue();
        
        
         log.debug("Entramos a childrenCount resultado" + count);

        return count;
    }
    
    
    public T wideFind(Object id){
    
        boolean isBorrable=true;
        T item = this.find(id);
        
        if (item == null){
            return item;
        }
        
        for (Field f : entityClass.getDeclaredFields()) {
            
            boolean hasToManyAnnotations = (f.isAnnotationPresent(OneToMany.class))
                   || (f.isAnnotationPresent(ManyToMany.class));

            if (hasToManyAnnotations) {

                try {
                    f.setAccessible(true);
                    isBorrable = isBorrable && ((List) f.get(item)).isEmpty();
                    f.setAccessible(false);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        
        
        
        
        return item;
    }
    
    
    public boolean borrable(Object id) {

        T item = this.find(id);
        
        log.debug("Entramos a borrable " + item);
        
        
        if (item == null) {
            return false;
        }
        
        log.debug("Entramos a borrable con no false " + item);
        
        for (Field f : entityClass.getDeclaredFields()) {
            boolean hasToManyAnnotations = (f.isAnnotationPresent(OneToMany.class))
                    || (f.isAnnotationPresent(ManyToMany.class));
            if (hasToManyAnnotations) {
                
                Type type = f.getGenericType();
                ParameterizedType pt = (ParameterizedType) type;
                
                List<Type> arguments = Arrays.asList(pt.getActualTypeArguments());
                
                Class childEntityClass = null;
                
                for (Type argtype: arguments){
                    childEntityClass = (Class) argtype;
                    break;
                }
                
                if (childEntityClass==null) continue;
                
                if  (this.childrenCount(id, childEntityClass, entityClass)>0){
                    
                    log.debug("Cuenta positiva");
                    
                    return false;
                }
            }
        }

        
         log.debug("Cuenta 0");
        
        return true;

    }
    
}
