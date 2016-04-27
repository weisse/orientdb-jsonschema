package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;

public class LinkedType extends AbstractConstraint{

	public LinkedType() {}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = OTypeJsonSchemaMap.getInstance();
		OType oType = oProperty.getLinkedType();
		if(oType != null){
			ObjectNode typeSchema = (ObjectNode) typeMap.get(oType);
			ObjectNode linkedTypeSchema = new ObjectNode(JsonNodeFactory.instance);
			ArrayNode constraints = this.getConstraints(propertySchema);
			switch(oProperty.getType()){
				case EMBEDDEDLIST:
					linkedTypeSchema.put("items", typeSchema);
					constraints.add(linkedTypeSchema);
				break;
				case EMBEDDEDSET:
					linkedTypeSchema.put("items", typeSchema);
					constraints.add(linkedTypeSchema);
				break;
				case EMBEDDEDMAP:
					linkedTypeSchema.put("additionalProperties", typeSchema);
					constraints.add(linkedTypeSchema);
				break;
			}
		}
	}

}
