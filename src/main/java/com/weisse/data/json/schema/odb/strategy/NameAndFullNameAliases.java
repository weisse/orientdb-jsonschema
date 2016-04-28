package com.weisse.data.json.schema.odb.strategy;

import java.util.Map;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.interfaces.PropertyNameStrategy;

public class NameAndFullNameAliases implements PropertyNameStrategy{

	private static final NameAndFullNameAliases INSTANCE = new NameAndFullNameAliases();
	
	public static NameAndFullNameAliases getInstance(){
		return INSTANCE;
	}
	
	private NameAndFullNameAliases() {}
	
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
