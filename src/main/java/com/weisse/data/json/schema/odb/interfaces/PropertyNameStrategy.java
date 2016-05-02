package com.weisse.data.json.schema.odb.interfaces;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;

public interface PropertyNameStrategy {
	
	public String apply(OProperty oProperty, OJsonSchemaConfiguration configuration);
	
}
