package es.caib.sgtsic.jsf;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import javax.faces.application.FacesMessage;

public class JSFUtil implements Serializable {

    public static void clearSubmittedValues(String formName) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot view = context.getViewRoot();
        UIComponent uiComponent = view.findComponent(formName);
        clearSubmittedValues(uiComponent);
    }

    /*
     * see: http://wiki.apache.org/myfaces/ClearInputComponents
     */
    public static void clearSubmittedValues(UIComponent uiComponent) {
        
        if (uiComponent == null) {
            return;
        }

        Iterator<UIComponent> children = (uiComponent).getFacetsAndChildren();
        while (children.hasNext()) {
            clearSubmittedValues(children.next());
        }
        if (uiComponent instanceof UIInput) {
            ((UIInput) uiComponent).setSubmittedValue(null);
            ((UIInput) uiComponent).setValue(null);
            ((UIInput) uiComponent).setLocalValueSet(false);
            ((UIInput) uiComponent).resetValue();
        }

    }
    
    public static void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
