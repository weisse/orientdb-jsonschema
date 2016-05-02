package com.weisse.data.json.schema.odb.generator;

import java.util.Collection;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OJsonSchemaFacade;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class OClassJsonSchemaGenerator {

	private OJsonSchemaConfiguration configuration;
	private OPropertyJsonSchemaGenerator propertySchemaGenerator;
	
	public OClassJsonSchemaGenerator(OJsonSchemaConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Lazy initialization of {@link OPropertyJsonSchemaGenerator}
	 * @return
	 */
	private OPropertyJsonSchemaGenerator getOPropertyJsonSchemaGenerator(){
		if(this.propertySchemaGenerator == null){
			this.propertySchemaGenerator =
					new OPropertyJsonSchemaGenerator(this.configuration);
		}
		return this.propertySchemaGenerator;
	}
	
	/**
	 * It returns the JsonSchema containing the URI that references the provided class
	 * @param oClass
	 * @return
	 */
	public ObjectNode getReference(OClass oClass){
		ObjectNode classReference = new ObjectNode(JsonNodeFactory.instance);
		classReference.put(
				JsonSchemaDraft4.$REF,
				this.configuration.getBaseURL() + "/classes/" + oClass.getName() + ".json"
		);
		return classReference;
	}
	
	/**
	 * It generate the JsonSchema of the provided class
	 * according to the mode provided [export or not]
	 * @param oClass
	 * @param export
	 * @return
	 */
	private ObjectNode generateSchema(OClass oClass, boolean export){
		ObjectNode jsonSchema = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode jsonAllOf = new ArrayNode(JsonNodeFactory.instance);
		Collection<OClass> superclasses = oClass.getSuperClasses();
		ObjectNode objectSchema = new ObjectNode(JsonNodeFactory.instance);
		objectSchema.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.OBJECT);
		ObjectNode jsonProperties = new ObjectNode(JsonNodeFactory.instance);
		Collection<OProperty> properties = oClass.declaredProperties();
		if(export){
			jsonSchema.put(JsonSchemaDraft4.ID, this.getReference(oClass).get(JsonSchemaDraft4.$REF));
			for(OClass superclass: superclasses){
				jsonAllOf.add(this.getReference(superclass));
			}
			for(OProperty property: properties){
				jsonProperties.put(
						this.configuration
								.getPropertyNameStrategy()
								.apply(property, this.configuration),
						this.getOPropertyJsonSchemaGenerator()
								.exportSchema(property)
				);
			}
		}else{
			for(OClass superclass: superclasses){
				jsonAllOf.add(
						this.getSchema(superclass)
				);
			}
			for(OProperty property: properties){
				jsonProperties.put(
						this.configuration
								.getPropertyNameStrategy()
								.apply(property, this.configuration),
						this.getOPropertyJsonSchemaGenerator()
								.getSchema(property)
				);
			}
		}
		objectSchema.put(JsonSchemaDraft4.PROPERTIES, jsonProperties);
		jsonAllOf.add(objectSchema);
		jsonSchema.put(JsonSchemaDraft4.ALL_OF, jsonAllOf);
		return jsonSchema;
	}
	
	/**
	 * It returns the JsonSchema of the provided class
	 * @param oClass
	 * @return
	 */
	public ObjectNode getSchema(OClass oClass){
		return this.generateSchema(oClass, false);
	}
	
	/**
	 * It returns the JsonSchema of the provided class
	 * filled with references instead of plain schema
	 * @param oClass
	 * @return
	 */
	public ObjectNode exportSchema(OClass oClass){
		return this.generateSchema(oClass, true);
	}
	
}
