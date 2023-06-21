package org.unibl.etf.mdp.buyer;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class TestValidate {

	public static void main(String[] args) {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File("./internetschema.xsd"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File("./test.xml")));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
