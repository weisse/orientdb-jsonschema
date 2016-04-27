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
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

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
				itemsSchema.put(JsonSchemaDraft4.ITEMS, (ObjectNode) typeMap.get(OType.LINK));
				constraints.add(itemsSchema);
			}else if(type == OType.LINKMAP){
				ObjectNode propertiesSchema = new ObjectNode(JsonNodeFactory.instance);
				propertiesSchema.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, (ObjectNode) typeMap.get(OType.LINK));
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
				requiredSchema.put(JsonSchemaDraft4.REQUIRED, requiredItems);
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
					itemsAllOfWrapper.put(JsonSchemaDraft4.ALL_OF, itemsAllOf);
					itemsSchema.put(JsonSchemaDraft4.ITEMS, itemsAllOfWrapper);
					constraints.add(itemsSchema);
				}else if(type == OType.EMBEDDEDMAP){
					ObjectNode propertiesSchema = new ObjectNode(JsonNodeFactory.instance);
					ObjectNode propertiesAllOfWrapper = new ObjectNode(JsonNodeFactory.instance);
					ArrayNode propertiesAllOf = new ArrayNode(JsonNodeFactory.instance);
					propertiesAllOf.add(classSchema);
					if(requiredItems.size() > 0){
						propertiesAllOf.add(requiredSchema);
					}
					propertiesAllOfWrapper.put(JsonSchemaDraft4.ALL_OF, propertiesAllOf);
					propertiesSchema.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, propertiesAllOfWrapper);
					constraints.add(propertiesSchema);
				}				
			}
		}
	}
	
}
