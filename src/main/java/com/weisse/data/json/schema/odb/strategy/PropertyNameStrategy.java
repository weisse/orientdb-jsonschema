package com.weisse.data.json.schema.odb.strategy;

import java.util.Map;
import java.util.Set;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;

public class PropertyNameStrategy {

	private static final PropertyNameStrategy INSTANCE = new PropertyNameStrategy();
	
	public static PropertyNameStrategy getInstance(){
		return INSTANCE;
	}
	
	private PropertyNameStrategy() {}
	
	/**
	 * It returns which name should be applied to the property
	 * @param oProperty
	 * @return
	 */
	public String apply(OProperty oProperty){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		Map<String,String> aliases = configuration.getAliases();
		String fullName = oProperty.getFullName();
		String alias = aliases.get(fullName);
		if(alias != null){
			return alias;
			
		}
		String name = oProperty.getName();
		alias = aliases.get(name);
		if(alias != null){
			return alias;
		}
		return name;
	}
	
}
