package es.caib.sgtsic.jsf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


public abstract class AbstractSesionManager {
    
    public static final String IDIOMA_ES = "es";
    public static final String IDIOMA_CA = "ca";
    
    protected static Log log = LogFactory.getLog(AbstractSesionManager.class);

    // INIT ------
    public String idioma;
    public String html;
    public String end;
    private AbstractManager manager;


    // CONSTRUCTOR -----
    public AbstractSesionManager() {
        establecerIdioma(IDIOMA_ES);
    }

    // INIT -----
    public void inicio() {
        establecerIdioma(IDIOMA_ES);
    }
    
    
    // ACTION ------
    public String getEnd() {
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) faces.getExternalContext().getRequest();
        /* delete jsf session */
        req.getSession().invalidate();
        return "END";
    }

    public String acabarSesion() {
        log.debug("acabarSesion");
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) faces.getExternalContext().getRequest();
        /* delete jsf session */
        req.getSession().invalidate();
        return "index";
    }

   
    public void load (){
        this.html = "inicio";
    }

    // GETTERS ------
    public String getIdioma() {
        return idioma;
    }
    
    public String obtenerIdioma(){
        log.debug("entram a getIdioma");
        String langLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        // Si no es igual a la locale ho setetjam
        if(!langLocale.equals(idioma))
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(idioma));
        return idioma;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setIdioma(String idioma){
        this.idioma = idioma;
    }
    
    private void establecerIdioma(String idioma) {
        setIdioma(idioma);
        log.info("set IDIOMA --> "+ idioma);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(idioma));
    }

    
    
    public AbstractManager getManager() {
        return manager;
    }

    public void setManager(AbstractManager manager) {
        this.manager = manager;
    }


    public String getCustomValue(String nameValue){
        
        if (nameValue==null) return "";
        String customValue = nameValue + this.manager.getClass().getSimpleName();
        return customValue;
        
    }
    
    public String getCustomBtn(String nameValue){
        if (nameValue==null) return "";
        String managerName = this.manager.getClass().getSimpleName();
        String customValue = "btn" + nameValue + managerName;
        return customValue;
    }
    
    public String getCustomEditForm(String nameValue){
        if (nameValue==null) return "";
        String managerName = this.manager.getClass().getSimpleName();
        String customValue = "btn" + nameValue + managerName;
        return customValue;
    }
    
    

}

