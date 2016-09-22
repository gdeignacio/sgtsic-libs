/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.sgtsic.jsf;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;

/**
 *
 * @author gdeignacio
 */
public class Lists implements Serializable {

    public static List<SelectItem> getSelectItemList(List list, Class keyClass, String key, String value) {

        List<SelectItem> selectList = new ArrayList<>();

        for (Object t : list) {
            try {
                Method getKey = t.getClass().getMethod(key);
                Method getValue = t.getClass().getMethod(value);
                Object id = getKey.invoke(t, (Object[]) null);
                String nombre = (String) getValue.invoke(t, (Object[]) null);
                selectList.add(new SelectItem(keyClass.cast(id), nombre));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Lists.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return selectList;
    }

    
    
    public static List<SelectItem> getSelectItemList(List list) {
        return getSelectItemList(list, Long.class, "getId", "getNombre");
    }

}
