package com.arkea.satd.sonar;

import org.sonar.api.Plugin;

import com.arkea.satd.sonar.xml.ApigeeXmlRulesDefinition;
import com.arkea.satd.sonar.xml.ApigeeXmlSensor;

public class ApigeePlugin implements Plugin {

	@Override
	public void define(Context context) {

		context.addExtensions(
			//Xml.class,
			ApigeeXmlRulesDefinition.class,
			ApigeeXmlSensor.class
		);
	}

}


/*
	LOG.info("ApigeePlugin ########################################");
	List<Class<?>> newExtensions = new ArrayList<Class<?>>();
	newExtensions.add(ApigeeRulesDefinition.class);
	newExtensions.add(ApigeeSensor.class);
	
	List<Class<?>> currentExtensions = context.getExtensions();
	
	for(Class c : currentExtensions) {
		LOG.info("extension : " + c + " - " + c.getName());
	}
	
	
	if(!currentExtensions.contains(Xml.class)) {
		newExtensions.add(Xml.class);
	}
	
	context.addExtensions(newExtensions);
*/