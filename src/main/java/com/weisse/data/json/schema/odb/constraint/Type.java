package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;

public class Type extends AbstractConstraint{
	
	public Type() {}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = OTypeJsonSchemaMap.getInstance();
		this.getConstraints(propertySchema).add(typeMap.get(oProperty.getType()));
	}

}
