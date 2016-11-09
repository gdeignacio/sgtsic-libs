/*
 * Copyright 2016 gdeignacio.
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
package es.caib.sgtsic.utils.faces;

import es.caib.sgtsic.utils.ejb.AbstractServiceInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gdeignacio
 * @param <E>
 */

public final class DataModel<E> implements Serializable {
    
    protected static Log log = LogFactory.getLog(DataModel.class);
    
    public DataModel(Class<E> entityClass, AbstractServiceInterface<E> service) { 
        this.allItems = new ArrayList<>();
        this.filteredItems = new ArrayList<>();
        this.current = null;
        this.id = new Long(0);
        this.entityClass = entityClass;
        this.service = service;
        
        populateLista();
        
        /*
        
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
        //populateListas();
        
        
        */
        
        
    }
    
    private final AbstractServiceInterface<E> service;
    private final Class<E> entityClass;
    private List<E> allItems;
    private List<E> filteredItems;
    private E current;
    
    private Object id;
    
    private Map<String, List> selectableItems;
    private Map<String, List> detailItems;

    public List<E> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<E> allItems) {
        this.allItems = allItems;
    }

    public List<E> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<E> filteredItems) {
        this.filteredItems = filteredItems;
    }

    public E getCurrent() {
        return current;
    }

    public void setCurrent(E current) {
        this.current = current;
    }

    public Map<String, List> getSelectableItems() {
        return selectableItems;
    }

    public void setSelectableItems(Map<String, List> selectableItems) {
        this.selectableItems = selectableItems;
    }

    public Map<String, List> getDetailItems() {
        return detailItems;
    }

    public void setDetailItems(Map<String, List> detailItems) {
        this.detailItems = detailItems;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    protected void populateLista() {
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Entramos a lista " + entityClass.getSimpleName());
        log.debug("---------------------------------------------------------------------------------------------------");
        this.allItems = service.findAll();
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("SIZE LISTA " + allItems.size());
        log.debug("---------------------------------------------------------------------------------------------------");
    
    }
    
    
    protected void create() throws InstantiationException, IllegalAccessException{
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Nuevo " + entityClass.getSimpleName());
        log.debug("---------------------------------------------------------------------------------------------------");
        this.current = entityClass.newInstance();
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Creado " + this.current.getClass().getCanonicalName());
        log.debug("---------------------------------------------------------------------------------------------------");
        
    }
    
    protected void edit() {
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Guardando " + current.getClass().getSimpleName() + " "  +  current.toString());
        log.debug("---------------------------------------------------------------------------------------------------");
       
        service.edit(current);
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Guardado " + current.getClass().getSimpleName() + " "  +  current.toString());
        log.debug("---------------------------------------------------------------------------------------------------");
        
        populateLista();
       
    }
    
    protected void find() {
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Recuperando " + entityClass.getSimpleName() + " id:" + this.id.toString());
        log.debug("---------------------------------------------------------------------------------------------------");
       
        this.current = service.find(this.id);
        
        if (current == null) {
            return;
        }
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Recuperado " + current.getClass().getSimpleName() + " "  +  current.toString());
        log.debug("---------------------------------------------------------------------------------------------------");
       
    }
    
    protected void remove() {
        
        if (current == null) {
            return;
        }
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Borrando " + current.getClass().getSimpleName() + " "  +  current.toString());
        log.debug("---------------------------------------------------------------------------------------------------");
       
        service.remove(current);
        this.current = null;
        
        log.debug("---------------------------------------------------------------------------------------------------");
        log.debug("Borrado ");
        log.debug("---------------------------------------------------------------------------------------------------");
       
        populateLista();
    }
 
    
}
