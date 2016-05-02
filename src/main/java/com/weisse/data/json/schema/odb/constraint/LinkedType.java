package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class LinkedType extends AbstractPropertyConstraint{

	public LinkedType(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OType oType = oProperty.getLinkedType();
		if(oType != null){
			ObjectNode typeSchema = (ObjectNode) this.getTypeMap()
														.get(oType)
														.apply(this.configuration);
			ObjectNode linkedTypeSchema = new ObjectNode(JsonNodeFactory.instance);
			ArrayNode constraints = this.getConstraints(propertySchema);
			switch(oProperty.getType()){
				case EMBEDDEDLIST:
					linkedTypeSchema.put(JsonSchemaDraft4.ITEMS, typeSchema);
					constraints.add(linkedTypeSchema);
				break;
				case EMBEDDEDSET:
					linkedTypeSchema.put(JsonSchemaDraft4.ITEMS, typeSchema);
					constraints.add(linkedTypeSchema);
				break;
				case EMBEDDEDMAP:
					linkedTypeSchema.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, typeSchema);
					constraints.add(linkedTypeSchema);
				break;
			}
		}
	}

}
