package com.weisse.data.json.schema.odb.interfaces;

import com.orientechnologies.orient.core.metadata.schema.OProperty;

public interface PropertyNameStrategy {
	
	public String apply(OProperty oProperty);
	
}
