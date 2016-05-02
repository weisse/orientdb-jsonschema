package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class NotNull extends AbstractPropertyConstraint{

	public NotNull(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		if(!oProperty.isNotNull()){
			ObjectNode nullSchema = new ObjectNode(JsonNodeFactory.instance);
			nullSchema.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NULL);
			ArrayNode alternatives = this.getAlternatives(propertySchema);
			alternatives.add(nullSchema);
		}
	}

}
