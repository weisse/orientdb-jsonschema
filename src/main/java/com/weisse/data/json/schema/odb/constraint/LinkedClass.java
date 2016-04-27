package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.OJsonSchemaFactory;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.generator.OClassJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.strategy.PropertyNameStrategy;
import com.weisse.data.json.schema.odb.strategy.RequiredPropertyStrategy;

public class LinkedClass extends AbstractConstraint{
	
	public LinkedClass() {}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = OTypeJsonSchemaMap.getInstance();
		OClass oClass = oProperty.getLinkedClass();
		if(oClass != null){
			OType type = oProperty.getType();
			ArrayNode constraints = this.getConstraints(propertySchema);
			if(type == OType.LINK){
				constraints.add((ObjectNode) typeMap.get(OType.LINK));
			}else if(
				type == OType.LINKLIST ||
				type == OType.LINKSET
			){
				ObjectNode itemsSchema = new ObjectNode(JsonNodeFactory.instance);
				itemsSchema.put("items", (ObjectNode) typeMap.get(OType.LINK));
				constraints.add(itemsSchema);
			}else if(type == OType.LINKMAP){
				ObjectNode propertiesSchema = new ObjectNode(JsonNodeFactory.instance);
				propertiesSchema.put("additionalProperties", (ObjectNode) typeMap.get(OType.LINK));
				constraints.add(propertiesSchema);
			}else{
				ObjectNode classSchema = null;
				if(export){
					classSchema = OClassJsonSchemaGenerator
												.getInstance()
												.getReference(oClass);
				}else{
					classSchema = OJsonSchemaFactory
												.getInstance()
												.getOJsonSchema(oClass)
												.getSchema();
				}
				RequiredPropertyStrategy requiredStrategy = RequiredPropertyStrategy.getInstance();
				ObjectNode requiredSchema = new ObjectNode(JsonNodeFactory.instance);
				ArrayNode requiredItems = new ArrayNode(JsonNodeFactory.instance);
				for(OProperty oClassProperty: oClass.properties()){
					if(requiredStrategy.isRequired(oClassProperty)){
						requiredItems.add(
							PropertyNameStrategy
								.getInstance()
								.apply(oClassProperty)
						);
					}
				}
				requiredSchema.put("required", requiredItems);
				if(type == OType.EMBEDDED){
					constraints.add(classSchema);
					if(requiredItems.size() > 0){
						constraints.add(requiredSchema);						
					}
				}else if(
					type == OType.EMBEDDEDLIST ||
					type == OType.EMBEDDEDSET
				){
					ObjectNode itemsSchema = new ObjectNode(JsonNodeFactory.instance);
					ObjectNode itemsAllOfWrapper = new ObjectNode(JsonNodeFactory.instance);
					ArrayNode itemsAllOf = new ArrayNode(JsonNodeFactory.instance);
					itemsAllOf.add(classSchema);
					if(requiredItems.size() > 0){
						itemsAllOf.add(requiredSchema);
					}
					itemsAllOfWrapper.put("allOf", itemsAllOf);
					itemsSchema.put("items", itemsAllOfWrapper);
					constraints.add(itemsSchema);
				}else if(type == OType.EMBEDDEDMAP){
					ObjectNode propertiesSchema = new ObjectNode(JsonNodeFactory.instance);
					ObjectNode propertiesAllOfWrapper = new ObjectNode(JsonNodeFactory.instance);
					ArrayNode propertiesAllOf = new ArrayNode(JsonNodeFactory.instance);
					propertiesAllOf.add(classSchema);
					if(requiredItems.size() > 0){
						propertiesAllOf.add(requiredSchema);
					}
					propertiesAllOfWrapper.put("allOf", propertiesAllOf);
					propertiesSchema.put("additionalProperties", propertiesAllOfWrapper);
					constraints.add(propertiesSchema);
				}				
			}
		}
	}
	
}
