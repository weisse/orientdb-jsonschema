package com.weisse.data.json.schema.odb.interfaces;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;

public interface RequiredPropertyStrategy {
	
	public boolean isRequired(OProperty oProperty, OJsonSchemaConfiguration configuration);
	
}
