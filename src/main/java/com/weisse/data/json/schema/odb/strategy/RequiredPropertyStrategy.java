package com.weisse.data.json.schema.odb.strategy;

import java.util.Set;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;

public class RequiredPropertyStrategy {

	private static final RequiredPropertyStrategy INSTANCE = new RequiredPropertyStrategy();
	
	private static final String OUT = "out";
	private static final String IN = "in";
	private static final String EDGE = "E";
	
	public static RequiredPropertyStrategy getInstance(){
		return INSTANCE;
	}
	
	private RequiredPropertyStrategy() {}
	
	/**
	 * It evaluates if a property is required or not, according to the configuration
	 * @param oProperty
	 * @return
	 */
	public boolean isRequired(OProperty oProperty){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		Set<String> required = configuration.getRequired();
		String fullName = oProperty.getFullName();
		if(required.contains(fullName)){
			return true;
		}else{
			String name = oProperty.getName();
			if(required.contains(name)){
				return true;
			}else{
				Set<String> unrequired = configuration.getUnrequired();
				if(
					oProperty.isMandatory() ||
					(
						oProperty.getOwnerClass().isSubClassOf(EDGE) &&
						(name.equals(OUT) || name.equals(IN))
					)
				){
					if(
						unrequired.contains(fullName) ||
						unrequired.contains(name)
					){
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
			}
		}
	}
	
}
