package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OJsonSchemaFacade;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.generator.OClassJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.interfaces.RequiredPropertyStrategy;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class LinkedClass extends AbstractPropertyConstraint{
	
	public LinkedClass(OJsonSchemaConfiguration configuration) {
		super(configuration);
	}
	
	@Override
	public void apply(OProperty oProperty, ObjectNode propertySchema, boolean export) {
		OTypeJsonSchemaMap typeMap = this.getTypeMap();
		OClass oClass = oProperty.getLinkedClass();
		if(oClass != null){
			OType type = oProperty.getType();
			ArrayNode constraints = this.getConstraints(propertySchema);
			if(type == OType.LINK){
				constraints.add((ObjectNode) typeMap.get(OType.LINK).apply(this.configuration));
			}else if(
				type == OType.LINKLIST ||
				type == OType.LINKSET
			){
				ObjectNode itemsSchema = new ObjectNode(JsonNodeFactory.instance);
				itemsSchema.put(
						JsonSchemaDraft4.ITEMS, 
						(ObjectNode) typeMap.get(OType.LINK).apply(this.configuration)
				);
				constraints.add(itemsSchema);
			}else if(type == OType.LINKMAP){
				ObjectNode propertiesSchema = new ObjectNode(JsonNodeFactory.instance);
				propertiesSchema.put(
						JsonSchemaDraft4.ADDITIONAL_PROPERTIES,
						(ObjectNode) typeMap.get(OType.LINK).apply(this.configuration)
				);
				constraints.add(propertiesSchema);
			}else{
				ObjectNode classSchema = null;
				if(export){
					classSchema = this.getOClassSchemaGenerator()
												.getReference(oClass);
				}else{
					classSchema = this.getOClassSchemaGenerator()
												.getSchema(oClass);
				}
				RequiredPropertyStrategy requiredStrategy = 
							this.configuration
										.getRequiredPropertyStrategy();
				ObjectNode requiredSchema = new ObjectNode(JsonNodeFactory.instance);
				ArrayNode requiredItems = new ArrayNode(JsonNodeFactory.instance);
				for(OProperty oClassProperty: oClass.properties()){
					if(requiredStrategy.isRequired(oClassProperty, this.configuration)){
						requiredItems.add(
							this.configuration
								.getPropertyNameStrategy()
								.apply(oClassProperty, this.configuration)
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
