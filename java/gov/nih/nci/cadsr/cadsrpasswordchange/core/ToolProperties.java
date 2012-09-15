package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.IOException;  
import java.io.InputStream;  
import java.util.Properties;  
  
 
public class ToolProperties {  
  
   
    private Properties props = null;
    
    static private ToolProperties instance;

    public static synchronized ToolProperties getInstance() throws IOException {
      if (instance == null)
   		  instance = new ToolProperties();
      return instance;
    }    
    private ToolProperties() throws IOException {
    	InputStream is = null;
    	try {
            ClassLoader cl = this.getClass().getClassLoader();
            is = cl.getResourceAsStream("cadsrpasswordchange.properties");
            props = new Properties();
            props.load(is);
        } catch (Exception e) {
            throw new IOException("Unable to get properties in ToolProperties() : " + e);
        } finally {
       		if (is != null)
       			is.close();
        }
    }
    
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}  
