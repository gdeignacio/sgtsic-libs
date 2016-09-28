/*
 * Copyright 2016 Conselleria de Salut. Govern de les Illes Balears
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.caib.sgtsic.utils.ejb;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gdeignacio
 * @param <T>
 */
public abstract class AbstractService<T> {
    
    protected static Log log = LogFactory.getLog(AbstractService.class);
    private final Class<T> entityClass;
    
    /**
     *
     * @param entityClass
     */
    public AbstractService(Class<T> entityClass) {
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
    
    
    public T wideFind(Object id) {

        boolean isBorrable = true;
        T item = this.find(id);

        if (item == null) {
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
                    Logger.getLogger(AbstractService.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        return item;
    }
    
    private List<Field> getMappedFields(){
    
        List<Field> lBound = new ArrayList<>();
        for (Field f: entityClass.getDeclaredFields()){
            if (f.getAnnotation(OneToMany.class)!=null || f.getAnnotation(ManyToMany.class)!=null){
                lBound.add(f);
                continue;
            }
            if (f.getAnnotation(OneToOne.class)!=null 
                    && !"".equals(f.getAnnotation(OneToOne.class).mappedBy())
                    ){
                lBound.add(f);
            }
        }
        return lBound;     
    }
    
    private Map<Field, Long> getMappedFieldsCardinal(T item){
        
        Map<Field, Long> mappedFieldsCardinal = new HashMap<>();
        
        for (Field f:getMappedFields()){
            Long cardinal = getMappedFieldCardinal(item, f);
            if (cardinal == 0) continue; 
            mappedFieldsCardinal.put(f, cardinal);
        }
        
        return mappedFieldsCardinal;
    }
    
    private Class getFieldClass(Field f){
        
        if (f == null) return null;
        
        if (f.getAnnotation(OneToOne.class)!=null){
            return f.getType();
        }
        
        Type type = f.getGenericType();
        ParameterizedType pt = (ParameterizedType) type;
        
        if (pt.getActualTypeArguments().length == 0) return null;
        
        return (Class)pt.getActualTypeArguments()[0];
       
    
    }
    
    
    
    
    private Long getMappedFieldCardinal(T item, Field f) {
        
        Class entityChildClass = getFieldClass(f);
        
        
        
        
        
        String qry = "select count(o) from " + entityChildClass.getSimpleName() + 
                " o where ";
        
       
      //  "SELECT e FROM CausaDecomisEntity e WHERE e.id = :id"
        
        String strQry = "SELECT e FROM CausaDecomisEntity e WHERE e.id = :id";
        
        return new Long(0);
    }
    
    
    public boolean disposable(Object id){
        
        T item = this.find(id);
        log.debug("Entramos a disposable" + item);
        if (item == null) return false;
        log.debug("Entramos a disposable con no false" + item);
        Map<Field, Long> cardinals = getMappedFieldsCardinal(item);
        return cardinals.keySet().isEmpty();
        
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

                for (Type argtype : arguments) {
                    childEntityClass = (Class) argtype;
                    break;
                }

                if (childEntityClass == null) {
                    continue;
                }

                if (this.childrenCount(id, childEntityClass, entityClass) > 0) {

                    log.debug("Cuenta positiva");

                    return false;
                }
            }
        }

        log.debug("Cuenta 0");

        return true;

    }

    

}
