package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class Minimum extends AbstractPropertyConstraint{

	public Minimum(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		String minString = oProperty.getMin();
		if(minString != null){
			ObjectNode minSchema = new ObjectNode(JsonNodeFactory.instance);
			int min = Integer.parseInt(minString);
			ArrayNode constraints = this.getConstraints(propertySchema);
			ObjectNode typeSchema = 
					(ObjectNode) this.getTypeMap()
											.get(oProperty.getType())
											.apply(this.configuration);
			switch(typeSchema.get(JsonSchemaDraft4.TYPE).asText()){
				case JsonSchemaDraft4.NUMBER:
					minSchema.put(JsonSchemaDraft4.MINIMUM, min);
					constraints.add(minSchema);
				break;
				case JsonSchemaDraft4.STRING:
					minSchema.put(JsonSchemaDraft4.MIN_LENGTH, min);
					constraints.add(minSchema);
				break;
				case JsonSchemaDraft4.ARRAY:
					minSchema.put(JsonSchemaDraft4.MIN_ITEMS, min);
					constraints.add(minSchema);
				break;
				case JsonSchemaDraft4.OBJECT:
					minSchema.put(JsonSchemaDraft4.MIN_PROPERTIES, min);
					constraints.add(minSchema);
				break;
			}
		}
	}

}
