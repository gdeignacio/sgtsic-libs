/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.ejb3;

import java.util.List;

/**
 *
 * @author gdeignacio
 * @param <T>
 */

public abstract interface AbstractFacadeLocal<T> {

    void create(T entity);

    void edit(T entity);

    void remove(T entity);

    T find(Object id);
    
    T wideFind(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);

    int count();

    public boolean borrable(Object id);
    
}
