package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;

public class Minimum extends AbstractConstraint{

	public Minimum() {}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = OTypeJsonSchemaMap.getInstance();
		String minString = oProperty.getMin();
		if(minString != null){
			ObjectNode minSchema = new ObjectNode(JsonNodeFactory.instance);
			int min = Integer.parseInt(minString);
			ArrayNode constraints = this.getConstraints(propertySchema);
			ObjectNode typeSchema = (ObjectNode) typeMap.get(oProperty.getType());
			switch(typeSchema.get("type").asText()){
				case "number":
					minSchema.put("minimum", min);
					constraints.add(minSchema);
				break;
				case "string":
					minSchema.put("minLength", min);
					constraints.add(minSchema);
				break;
				case "array":
					minSchema.put("minItems", min);
					constraints.add(minSchema);
				break;
				case "object":
					minSchema.put("minProperties", min);
					constraints.add(minSchema);
				break;
			}
		}
	}

}
