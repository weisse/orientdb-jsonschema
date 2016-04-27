package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;

public class NotNull extends AbstractConstraint{

	public NotNull() {}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		if(!oProperty.isNotNull()){
			ObjectNode nullSchema = new ObjectNode(JsonNodeFactory.instance);
			nullSchema.put("type", "null");
			ArrayNode alternatives = this.getAlternatives(propertySchema);
			alternatives.add(nullSchema);
		}
	}

}
