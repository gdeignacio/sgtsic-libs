package es.caib.sgtsic.util.bitel;

import org.directwebremoting.create.AbstractCreator;
import org.directwebremoting.extend.Creator;

/**
 * Clase necesaria para poder instanciar EJB's desde la capa JavaScript mediante DWR
 * @author FRO
 * @version 1.0 22/10/2008
 */
public class EJBCreator extends AbstractCreator implements Creator {

	private String JNDIName;
	private Class remote;
	private Class home;
	
	public void setJndiName(String JNDIName) {
		this.JNDIName = JNDIName; 
	}
	public String getJndiName() {
		return JNDIName;
	}

	public Object getInstance() throws InstantiationException {
		return BitJsp.lookup(home, remote, JNDIName);
	}

	
	public void setRemoteInterface(String remote) {
		try {
			this.remote = Thread.currentThread().getContextClassLoader().loadClass(remote);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			BitLog.debug("EJBCreator: Cannot load class " + remote);
		}
	}
	public void setHomeInterface(String home) {
		try {
			this.home = Thread.currentThread().getContextClassLoader().loadClass(home);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			BitLog.debug("EJBCreator: Cannot load class " + home);
		}
	}
	
	public Class getType() {
		return remote;
	}
	
	

	
}
