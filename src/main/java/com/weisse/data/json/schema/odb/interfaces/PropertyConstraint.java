package com.weisse.data.json.schema.odb.interfaces;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;

public interface PropertyConstraint {
	
	void apply(OProperty oProperty, ObjectNode propertySchema, boolean export);
	
}
