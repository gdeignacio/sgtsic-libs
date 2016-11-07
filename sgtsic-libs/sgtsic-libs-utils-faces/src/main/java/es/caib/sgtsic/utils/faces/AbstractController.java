package es.caib.sgtsic.utils.faces;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.sgtsic.utils.ejb.AbstractServiceInterface;
import javax.faces.event.ActionEvent;

public abstract class AbstractController<E> {

    protected static Log log = LogFactory.getLog(AbstractController.class);

    private static final String GETID = "getId";

    private final Class<E> entityClass;

    private TableModel<E> tableModel;
    private DataModel<E> dataModel;    
    private Map<String, String> confirmMsg;


    public AbstractController(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract AbstractServiceInterface<E> getService();

    public abstract void load();

    public TableModel<E> getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel<E> tableModel) {
        this.tableModel = tableModel;
    }

    public DataModel<E> getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel<E> dataModel) {
        this.dataModel = dataModel;
    }
    

    public Map<String, String> getConfirmMsg() {
        return confirmMsg;
    }

    public void setConfirmMsg(Map<String, String> confirmMsg) {
        this.confirmMsg = confirmMsg;
    }

    public void inicio() {
        this.tableModel = new TableModel<>(entityClass);
        this.dataModel = new DataModel<>(entityClass);
        this.dataModel.setService(this.getService());
    }

    public void debug(ActionEvent actionEvent) {
        log.debug("---------------------------------------------------------------------------");
        log.debug("A DEBUG MESSAGE: " + actionEvent.toString());
        log.debug("---------------------------------------------------------------------------");
    }

    public void create(ActionEvent actionEvent) {
        debug(actionEvent);
        try {
            dataModel.create();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieve(ActionEvent actionEvent) {
        debug(actionEvent);
        dataModel.find();
    }

    public void update(ActionEvent actionEvent) {
        debug(actionEvent);
        dataModel.edit();
    }

    public void delete(ActionEvent actionEvent) {
        debug(actionEvent);
        dataModel.remove();
    }

}
