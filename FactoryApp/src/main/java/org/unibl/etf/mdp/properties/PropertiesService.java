package org.unibl.etf.mdp.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.distributor.ChooseWhoToBuyFromForm;

//Read config properties
public class PropertiesService {

	static {
		try {
			String LOGGER_PATH = PropertiesService.getElement("LOGGER_PATH");
			Handler fileHandler = new FileHandler(LOGGER_PATH, true);
			Logger.getLogger(PropertiesService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(PropertiesService.class.getName()).addHandler(fileHandler);
		} catch(IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	public static String getElement(String key) {
		try {
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream("."+File.separator+"config.properties");
			properties.load(fis);
			return properties.getProperty(key);
		} catch(Exception e) {
			Logger.getLogger(PropertiesService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
		return null;
	}
	
}

