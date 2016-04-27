package com.weisse.data.json.schema.odb.generator;

import java.util.Collection;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OJsonSchemaFactory;
import com.weisse.data.json.schema.odb.enumeration.DispositionSchema;
import com.weisse.data.json.schema.odb.strategy.PropertyNameStrategy;
import com.weisse.data.json.schema.odb.strategy.RequiredPropertyStrategy;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class DispositionJsonSchemaGenerator {

	private static final DispositionJsonSchemaGenerator INSTANCE = new DispositionJsonSchemaGenerator();
	
	public static final DispositionJsonSchemaGenerator getInstance(){
		return INSTANCE;
	}
	
	private DispositionJsonSchemaGenerator() {}
	
	/**
	 * It generates the JsonSchema of a creation disposition
	 * @param oClass
	 * @param export
	 * @return
	 */
	private ObjectNode generateCreateSchema(OClass oClass, boolean export){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		RequiredPropertyStrategy requiredStrategy = RequiredPropertyStrategy.getInstance();
		ObjectNode requiredSchema = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode requiredItems = new ArrayNode(JsonNodeFactory.instance);
		Collection<OProperty> properties = oClass.properties();
		for(OProperty property: properties){
			if(requiredStrategy.isRequired(property)){
				requiredItems.add(
					PropertyNameStrategy
									.getInstance()
									.apply(property)
				);
			}
		}
		requiredSchema.put(JsonSchemaDraft4.REQUIRED, requiredItems);
		ObjectNode createSchema = new ObjectNode(JsonNodeFactory.instance);
		ObjectNode classSchema;
		if(export){
			createSchema.put(JsonSchemaDraft4.ID, configuration.getBaseURL() + "/create/" + oClass.getName() + ".json");
			classSchema = OClassJsonSchemaGenerator
									.getInstance()
									.getReference(oClass);
		}else{
			classSchema = OJsonSchemaFactory
									.getInstance()
									.getOJsonSchema(oClass)
									.getSchema();
		}
		ArrayNode allOf = new ArrayNode(JsonNodeFactory.instance);
		allOf.add(classSchema);
		if(requiredItems.size() > 0){
			allOf.add(requiredSchema);
		}
		createSchema.put(JsonSchemaDraft4.ALL_OF, allOf);
		return createSchema;
	}

	/**
	 * It generates the JsonSchema of a multi creation disposition
	 * @param oClass
	 * @param export
	 * @return
	 */
	private ObjectNode generateMultiCreateSchema(OClass oClass, boolean export){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		ObjectNode definitions = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode oneOf = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode single = new ObjectNode(JsonNodeFactory.instance);
		single.put(JsonSchemaDraft4.$REF, "#/definitions/single");
		ObjectNode multiple = new ObjectNode(JsonNodeFactory.instance);
		multiple.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
		ObjectNode items = new ObjectNode(JsonNodeFactory.instance);
		items.put(JsonSchemaDraft4.$REF, "#/definitions/single");
		multiple.put(JsonSchemaDraft4.ITEMS, items);
		multiple.put(JsonSchemaDraft4.MIN_ITEMS, 1);
		oneOf.add(single);
		oneOf.add(multiple);
		ObjectNode multiCreateSchema = new ObjectNode(JsonNodeFactory.instance);
		if(export){
			definitions.put("single", configuration.getBaseURL() + "/create/" + oClass.getName() + ".json");
			multiCreateSchema.put(JsonSchemaDraft4.ID, configuration.getBaseURL() + "/multicreate/" + oClass.getName() + ".json");
		}else{
			definitions.put(
					"single", 
					OJsonSchemaFactory
									.getInstance()
									.getOJsonSchema(DispositionSchema.CREATE, oClass)
									.getSchema()
			);
		}
		multiCreateSchema.put(JsonSchemaDraft4.DEFINITIONS, definitions);
		multiCreateSchema.put(JsonSchemaDraft4.ONE_OF, oneOf);
		return multiCreateSchema;
	}
	
	/**
	 * It generates the JsonSchema of a patch disposition
	 * It is mandatory to provide at least one property
	 * @param oClass
	 * @param export
	 * @return
	 */
	private ObjectNode generatePatchSchema(OClass oClass, boolean export){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		ObjectNode patchSchema = new ObjectNode(JsonNodeFactory.instance);
		ObjectNode classSchema;
		ObjectNode minPropertiesSchema = new ObjectNode(JsonNodeFactory.instance);
		minPropertiesSchema.put(JsonSchemaDraft4.MIN_PROPERTIES, 1);
		if(export){
			patchSchema.put(JsonSchemaDraft4.ID, configuration.getBaseURL() + "/patch/" + oClass.getName() + ".json");
			classSchema = OClassJsonSchemaGenerator
									.getInstance()
									.getReference(oClass);
		}else{
			classSchema = OJsonSchemaFactory
									.getInstance()
									.getOJsonSchema(oClass)
									.getSchema();
		}
		ArrayNode allOf = new ArrayNode(JsonNodeFactory.instance);
		allOf.add(classSchema);
		allOf.add(minPropertiesSchema);
		patchSchema.put(JsonSchemaDraft4.ALL_OF, allOf);
		return patchSchema;
	}
	
	/**
	 * It generates the JsonSchema of a patch disposition
	 * It is mandatory to provide at least one property defined for the class
	 * @param oClass
	 * @param export
	 * @return
	 */
	private ObjectNode generateStrictPatchSchema(OClass oClass, boolean export){
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		ObjectNode anyOfSchema = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode anyOfSchemaCollection = new ArrayNode(JsonNodeFactory.instance);
		Collection<OProperty> properties = oClass.properties();
		for(OProperty property: properties){
			ObjectNode requiredPropertyWrapper = new ObjectNode(JsonNodeFactory.instance);
			ArrayNode requiredProperty = new ArrayNode(JsonNodeFactory.instance);
			requiredProperty.add(
					PropertyNameStrategy
									.getInstance()
									.apply(property)
			);
			requiredPropertyWrapper.put(JsonSchemaDraft4.REQUIRED, requiredProperty);
			anyOfSchemaCollection.add(requiredPropertyWrapper);
		}
		anyOfSchema.put(JsonSchemaDraft4.ANY_OF, anyOfSchemaCollection);
		ObjectNode patchSchema = new ObjectNode(JsonNodeFactory.instance);
		ObjectNode classSchema;
		if(export){
			patchSchema.put(JsonSchemaDraft4.ID, configuration.getBaseURL() + "/strictPatch/" + oClass.getName() + ".json");
			classSchema = OClassJsonSchemaGenerator
									.getInstance()
									.getReference(oClass);
		}else{
			classSchema = OJsonSchemaFactory
									.getInstance()
									.getOJsonSchema(oClass)
									.getSchema();
		}
		ArrayNode allOf = new ArrayNode(JsonNodeFactory.instance);
		allOf.add(classSchema);
		if(anyOfSchemaCollection.size() > 0){
			allOf.add(anyOfSchema);
		}
		patchSchema.put(JsonSchemaDraft4.ALL_OF, allOf);
		return patchSchema;
	}
	
	/**
	 * It returns the full JsonSchema of a create disposition
	 * @param oClass
	 * @return
	 */
	public ObjectNode getCreateSchema(OClass oClass){
		return this.generateCreateSchema(oClass, false);
	}
	
	/**
	 * It returns the JsonSchema of a create disposition with references
	 * @param oClass
	 * @return
	 */
	public ObjectNode exportCreateSchema(OClass oClass){
		return this.generateCreateSchema(oClass, true);
	}
	
	/**
	 * It returns the full JsonSchema of a multi create disposition
	 * @param oClass
	 * @return
	 */
	public ObjectNode getMultiCreateSchema(OClass oClass){
		return this.generateMultiCreateSchema(oClass, false);
	}
	
	/**
	 * It returns the JsonSchema of a multi create disposition with references
	 * @param oClass
	 * @return
	 */
	public ObjectNode exportMultiCreateSchema(OClass oClass){
		return this.generateMultiCreateSchema(oClass, true);
	}
	
	/**
	 * It returns the full JsonSchema of a patch disposition
	 * @param oClass
	 * @return
	 */
	public ObjectNode getPatchSchema(OClass oClass){
		return this.generatePatchSchema(oClass, false);
	}
	
	/**
	 * It returns the JsonSchema of a patch disposition with references
	 * @param oClass
	 * @return
	 */
	public ObjectNode exportPatchSchema(OClass oClass){
		return this.generatePatchSchema(oClass, true);
	}
	
	/**
	 * It return the full JsonSchema of a strict patch disposition
	 * @param oClass
	 * @return
	 */
	public ObjectNode getStrictPatchSchema(OClass oClass){
		return this.generateStrictPatchSchema(oClass, false);
	}
	
	/**
	 * It returns the JsonSchema of a strict patch disposition with references
	 * @param oClass
	 * @return
	 */
	public ObjectNode exportStrictPatchSchema(OClass oClass){
		return this.generateStrictPatchSchema(oClass, true);
	}
}
