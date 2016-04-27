package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;

public class Maximum extends AbstractConstraint{

	public Maximum() {}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = OTypeJsonSchemaMap.getInstance();
		String maxString = oProperty.getMax();
		if(maxString != null){
			ObjectNode maxSchema = new ObjectNode(JsonNodeFactory.instance);
			int max = Integer.parseInt(maxString);
			ArrayNode constraints = this.getConstraints(propertySchema);
			ObjectNode typeSchema = (ObjectNode) typeMap.get(oProperty.getType());
			switch(typeSchema.get("type").asText()){
				case "number":
					maxSchema.put("maximum", max);
					constraints.add(maxSchema);
				break;
				case "string":
					maxSchema.put("maxLength", max);
					constraints.add(maxSchema);
				break;
				case "array":
					maxSchema.put("maxItems", max);
					constraints.add(maxSchema);
				break;
				case "object":
					maxSchema.put("maxProperties", max);
					constraints.add(maxSchema);
				break;
			}
		}
	}

}
