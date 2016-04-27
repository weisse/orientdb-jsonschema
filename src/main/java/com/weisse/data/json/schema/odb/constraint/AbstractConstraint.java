package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.weisse.data.json.schema.odb.interfaces.PropertyConstraint;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public abstract class AbstractConstraint implements PropertyConstraint {
	
	protected ArrayNode getAlternatives(ObjectNode propertySchema){
		return (ArrayNode) propertySchema.get(JsonSchemaDraft4.ONE_OF);
	}
	
	protected ArrayNode getConstraints(ObjectNode propertySchema){
		return (ArrayNode) this.getAlternatives(propertySchema).get(0).get(JsonSchemaDraft4.ALL_OF);
	}

}
