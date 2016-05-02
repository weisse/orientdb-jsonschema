package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class Maximum extends AbstractPropertyConstraint{

	public Maximum(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		String maxString = oProperty.getMax();
		if(maxString != null){
			ObjectNode maxSchema = new ObjectNode(JsonNodeFactory.instance);
			int max = Integer.parseInt(maxString);
			ArrayNode constraints = this.getConstraints(propertySchema);
			ObjectNode typeSchema = 
					(ObjectNode) this.getTypeMap()
											.get(oProperty.getType())
											.apply(this.configuration);
			switch(typeSchema.get(JsonSchemaDraft4.TYPE).asText()){
				case JsonSchemaDraft4.NUMBER:
					maxSchema.put(JsonSchemaDraft4.MAXIMUM, max);
					constraints.add(maxSchema);
				break;
				case JsonSchemaDraft4.STRING:
					maxSchema.put(JsonSchemaDraft4.MAX_LENGTH, max);
					constraints.add(maxSchema);
				break;
				case JsonSchemaDraft4.ARRAY:
					maxSchema.put(JsonSchemaDraft4.MAX_ITEMS, max);
					constraints.add(maxSchema);
				break;
				case JsonSchemaDraft4.OBJECT:
					maxSchema.put(JsonSchemaDraft4.MAX_PROPERTIES, max);
					constraints.add(maxSchema);
				break;
			}
		}
	}

}
