package org.unibl.etf.mdp.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesService {

	public static String getElement(String key) {
		try {
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream("."+File.separator+"config.properties");
			properties.load(fis);
			return properties.getProperty(key);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
