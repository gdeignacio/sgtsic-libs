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

/**
 *
 * @author gdeignacio
 */
public class FieldModel implements Serializable {
    
        private String label;
        private String property;
        private Class clazz;
 
        public FieldModel(String label, String property, Class clazz) {
            this.label = label;
            this.property = property;
            this.clazz = clazz;
        }
       
        public String getHeader() {
            return label;
        }
 
        public String getProperty() {
            return property;
        }
        
        public Class getClazz(){
            return clazz;
        }
    
        
}
