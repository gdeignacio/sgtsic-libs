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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author gdeignacio
 * @param <E>
 */
public class FormModel<E> implements Serializable {
    
    // View management
    public FormModel(Class<E> clazz) {
        this.columns = createDynamicColumns(clazz);
        this.fields = createDynamicFields(clazz);
    }
    
    private static final List<String> VALID_COLUMN_KEYS = new ArrayList<>(); //Arrays.asList("id", "brand", "year", "color", "price");
    private String columnTemplate = ""; //id brand year";
    
    private List<FieldModel> columns;
    private List<FieldModel> fields;

    public List<FieldModel> getColumns() {
        return columns;
    }
    public void setColumns(List<FieldModel> columns) {
        this.columns = columns;
    }
    public List<FieldModel> getFields() {
        return fields;
    }
    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }
    
    private List<FieldModel> createDynamicColumns(Class<E> clazz) {
        
        //org.primefaces.component.selectonemenu.SelectOneMenu
        
        
        
        List<FieldModel> dynamicColumns = new ArrayList<>();
        for (Field f: clazz.getDeclaredFields()){
            if (f.isAnnotationPresent(Column.class)) {
                dynamicColumns.add(new FieldModel(f.getName().toUpperCase(), f.getName(), InputText.class));
            }
        }
        return dynamicColumns;    
    }
    
    private List<FieldModel> createDynamicFields(Class<E> clazz) {
        
        List<FieldModel> dynamicFields = new ArrayList<>();
       
        for (Field f: clazz.getDeclaredFields()){
            
            if (f.isAnnotationPresent(Column.class)) {
                dynamicFields.add(new FieldModel(f.getName().toUpperCase(), f.getName(), InputText.class));
            }
            
            if (f.isAnnotationPresent(JoinColumn.class)){
                if (((f.isAnnotationPresent(OneToOne.class)) || f.isAnnotationPresent(ManyToOne.class))) {
                    dynamicFields.add(new FieldModel(f.getName().toUpperCase(), f.getName(), SelectOneMenu.class));
                }
            }  
        }
        return dynamicFields;    
    }
    
}
