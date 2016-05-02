package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;

public class Type extends AbstractPropertyConstraint{
	
	public Type(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		this.getConstraints(propertySchema).add(
			this.getTypeMap().get(oProperty.getType()).apply(this.configuration)
		);
	}

}
