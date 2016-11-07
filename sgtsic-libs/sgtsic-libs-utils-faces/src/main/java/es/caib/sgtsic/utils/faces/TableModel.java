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

/**
 *
 * @author gdeignacio
 * @param <E>
 */
public class TableModel<E> implements Serializable {
    
    // View management
    public TableModel(Class<E> clazz) {
        this.columns = createDynamicColumns(clazz);
        this.fields = createDynamicFields(clazz);
    }
    
    private static final List<String> VALID_COLUMN_KEYS = new ArrayList<>(); //Arrays.asList("id", "brand", "year", "color", "price");
    private String columnTemplate = ""; //id brand year";
    
    private List<ColumnModel> columns;
    private List<ColumnModel> fields;

    public List<ColumnModel> getColumns() {
        return columns;
    }
    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }
    public List<ColumnModel> getFields() {
        return fields;
    }
    public void setFields(List<ColumnModel> fields) {
        this.fields = fields;
    }
    
    private List<ColumnModel> createDynamicColumns(Class<E> clazz) {
        
        List<ColumnModel> dynamicColumns = new ArrayList<>();
        for (Field f: clazz.getDeclaredFields()){
            if (f.isAnnotationPresent(Column.class)) {
                dynamicColumns.add(new ColumnModel(f.getName().toUpperCase(), f.getName()));
            }
        }
        return dynamicColumns;    
    }
    
    private List<ColumnModel> createDynamicFields(Class<E> clazz) {
        
        List<ColumnModel> dynamicFields = new ArrayList<>();
        for (Field f: clazz.getDeclaredFields()){
            dynamicFields.add(new ColumnModel(f.getName().toUpperCase(), f.getName()));
        }
        return dynamicFields;    
    }
    
}
