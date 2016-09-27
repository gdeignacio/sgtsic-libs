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
package es.caib.sgtsic.utils.ejb;

/**
 *
 * @author gdeignacio
 */
public class JNDI {
    
   // jndi:MATADEROS-ear-1.0-SNAPSHOT/SectorSanitarioFacade/local-es.caib.mataderos.back.persistence.ejb3.SectorSanitarioFacadeLocal
    
   // public static final String SECTOR_SANITARIO_FACADE_LOCAL =          
            
    //public static final String unitName = "defaultPU";        
    
    /*
    public static final String groupid = "es.caib";
    public static final String nomapp = "MATADEROS";
    public static final String packaging = "ear";
    public static final String version = "1.0-SNAPSHOT";
    public static final String codapp = "es.caib.mataderos";
    
    
    public static final String back_ejb = codapp + ".back.ejb";
    public static final String back_ejb3 = codapp + ".back.persistence.ejb3";
    public static final String JNDIPackaging = nomapp + "-" + packaging + "-" + version + "/";
  
    public static final String AbastecedorFacadeLocal = JNDIPackaging + "AbastecedorFacade/local-" + back_ejb3 + ".AbastecedorFacadeLocal";
    public static final String DecomisoFacadeLocal = JNDIPackaging + "DecomisoFacade/local-" + back_ejb3 + ".DecomisoFacadeLocal";
    public static final String EnfermedadFacadeLocal = JNDIPackaging + "EnfermedadFacade/local-" + back_ejb3 + ".EnfermedadFacadeLocal";
    public static final String EspecieFacadeLocal = JNDIPackaging + "EspecieFacade/local-" + back_ejb3 + ".EspecieFacadeLocal";    
    public static final String InspectorFacadeLocal = JNDIPackaging + "InspectorFacade/local-" + back_ejb3 + ".InspectorFacadeLocal";
    public static final String LocalidadFacadeLocal = JNDIPackaging + "LocalidadFacade/local-" + back_ejb3 + ".LocalidadFacadeLocal";
    public static final String MataderoFacadeLocal = JNDIPackaging + "MataderoFacade/local-" + back_ejb3 + ".MataderoFacadeLocal";
    public static final String ParteFacadeLocal = JNDIPackaging + "ParteFacade/local-" + back_ejb3 + ".ParteFacadeLocal";
    public static final String SectorSanitarioFacadeLocal = JNDIPackaging + "SectorSanitarioFacade/local-" + back_ejb3 + ".SectorSanitarioFacadeLocal";
    public static final String SubtipoDecomisoFacadeLocal = JNDIPackaging + "SubtipoDecomisoFacade/local-" + back_ejb3 + ".SubtipoDecomisoFacadeLocal";
    public static final String TipoDecomisoFacadeLocal = JNDIPackaging + "TipoDecomisoFacade/local-" + back_ejb3 + ".TipoDecomisoFacadeLocal";
    public static final String TipoGanadoFacadeLocal = JNDIPackaging + "TipoGanadoFacade/local-" + back_ejb3 + ".TipoGanadoFacadeLocal";
 */
    
    public static String getJndiPackaging(Class cl){
        return cl.getPackage().getSpecificationTitle() + "/";
    } 
    
    public static String getJndiPackaging(String appBundleName){
        return appBundleName + "/";
    }
    
    /**
     *
     * @param cl
     * @return
     */
    public static String getJndiName(String appBundleName, Class cl){
        return getJndiPackaging(appBundleName) + cl.getSimpleName() + "/local-" + cl.getCanonicalName() ;
    }
    
     /**
     *
     * @param cl
     * @return
     */
    public static String getJndiName(Class cl){
        return getJndiPackaging(cl) + cl.getSimpleName() + "/local-" + cl.getCanonicalName() ;
    }
    
    
    public static String getFacadeLocalClassName(Class entity){
        return entity.getSimpleName() + "FacadeLocal";
    }
    
    
    public static String getServiceInterfaceClassName(Class entity){
        return entity.getSimpleName() + "ServiceInterface";
    }
    
    
    
    
}
