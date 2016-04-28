package com.weisse.data.json.schema.odb.interfaces;

import com.orientechnologies.orient.core.metadata.schema.OProperty;

public interface RequiredPropertyStrategy {
	
	public boolean isRequired(OProperty oProperty);
	
}
