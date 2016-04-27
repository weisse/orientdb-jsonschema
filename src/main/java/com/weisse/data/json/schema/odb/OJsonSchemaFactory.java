package com.weisse.data.json.schema.odb;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.enumeration.DispositionSchema;
import com.weisse.data.json.schema.odb.generator.DispositionJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.generator.OClassJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.generator.OPropertyJsonSchemaGenerator;

public class OJsonSchemaFactory {

	private static final OJsonSchemaFactory INSTANCE = new OJsonSchemaFactory();
	
	public static final OJsonSchemaFactory getInstance(){
		return INSTANCE;
	}
	
	private Map<OClass, OJsonSchema> classSchemas = new WeakHashMap<OClass, OJsonSchema>();
	private Map<OProperty, OJsonSchema> propertySchemas = new WeakHashMap<OProperty, OJsonSchema>();
	private Map<DispositionSchema, Map<OClass, OJsonSchema>> dispositionSchemas = 
			new HashMap<DispositionSchema, Map<OClass, OJsonSchema>>();
	
	private OJsonSchemaFactory() {
		for(DispositionSchema disposition: DispositionSchema.values()){
			dispositionSchemas.put(disposition, new WeakHashMap<OClass, OJsonSchema>());
		}
	}
	
	/**
	 * It returns the OJsonSchema of the provided class
	 * @param oClass
	 * @return
	 */
	public OJsonSchema getOJsonSchema(OClass oClass){
		OJsonSchema schema = classSchemas.get(oClass);
		if(schema == null){
			ObjectNode classSchema = 
					OClassJsonSchemaGenerator
								.getInstance()
								.getSchema(oClass);
			try {
				schema = new OJsonSchema(classSchema);
				classSchemas.put(oClass, schema);
			} catch (ProcessingException e) {
				e.printStackTrace();
			}
		}
		return schema;
	}
	
	/**
	 * It returns the OJsonSchema of the provided property
	 * @param oProperty
	 * @return
	 */
	public OJsonSchema getOJsonSchema(OProperty oProperty){
		OJsonSchema schema = propertySchemas.get(oProperty);
		if(schema == null){
			ObjectNode propertySchema = 
					OPropertyJsonSchemaGenerator
									.getInstance()
									.getSchema(oProperty);
			try {
				schema = new OJsonSchema(propertySchema);
				propertySchemas.put(oProperty, schema);
			} catch (ProcessingException e) {
				e.printStackTrace();
			}
		}
		return schema;
	}
	
	/**
	 * It returns the OJsonSchema of the provided disposition of the provided class
	 * @param disposition
	 * @param oClass
	 * @return
	 */
	public OJsonSchema getOJsonSchema(DispositionSchema disposition, OClass oClass){
		Map<OClass, OJsonSchema> dispositionMap = dispositionSchemas.get(disposition);
		OJsonSchema schema = dispositionMap.get(oClass);
		if(schema == null){
			ObjectNode classSchema = null;
			switch(disposition){
				case CREATE:
					classSchema = 
						DispositionJsonSchemaGenerator
										.getInstance()
										.getCreateSchema(oClass);
				break;
				case MULTI_CREATE:
					classSchema = 
						DispositionJsonSchemaGenerator
										.getInstance()
										.getMultiCreateSchema(oClass);
				break;
				case PATCH:
					classSchema =
						DispositionJsonSchemaGenerator
										.getInstance()
										.getPatchSchema(oClass);
				break;
				case STRICT_PATCH:
					classSchema = 
						DispositionJsonSchemaGenerator
										.getInstance()
										.getStrictPatchSchema(oClass);
				break;
			}
			try {
				schema = new OJsonSchema(classSchema);
				dispositionMap.put(oClass, schema);
			} catch (ProcessingException e) {
				e.printStackTrace();
			}
		}
		return schema;
	}
	
}
