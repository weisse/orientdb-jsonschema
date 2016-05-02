package com.weisse.data.json.schema.odb;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchemaProxy;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.weisse.data.json.schema.odb.enumeration.DispositionSchema;
import com.weisse.data.json.schema.odb.generator.DispositionJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.generator.OClassJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.generator.OPropertyJsonSchemaGenerator;

/**
 * A simple interface useful to build OJsonSchema objects
 * @author weisse
 *
 */
public class OJsonSchemaFacade {
	
	private OJsonSchemaConfiguration configuration;
	private OClassJsonSchemaGenerator classSchemaGenerator;
	private OPropertyJsonSchemaGenerator propertySchemaGenerator;
	private DispositionJsonSchemaGenerator dispositionSchemaGenerator;
	private Map<OClass, OJsonSchema> classSchemas = new WeakHashMap<OClass, OJsonSchema>();
	private Map<OProperty, OJsonSchema> propertySchemas = new WeakHashMap<OProperty, OJsonSchema>();
	private Map<DispositionSchema, Map<OClass, OJsonSchema>> dispositionSchemas = 
			new HashMap<DispositionSchema, Map<OClass, OJsonSchema>>();
	
	public OJsonSchemaFacade(OJsonSchemaConfiguration configuration) {
		this.configuration = configuration;
		this.classSchemaGenerator = new OClassJsonSchemaGenerator(this.configuration);
		this.propertySchemaGenerator = new OPropertyJsonSchemaGenerator(this.configuration);
		this.dispositionSchemaGenerator = new DispositionJsonSchemaGenerator(this.configuration);
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
					classSchemaGenerator
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
					propertySchemaGenerator
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
						dispositionSchemaGenerator
										.getCreateSchema(oClass);
				break;
				case MULTI_CREATE:
					classSchema = 
						dispositionSchemaGenerator
										.getMultiCreateSchema(oClass);
				break;
				case PATCH:
					classSchema =
						dispositionSchemaGenerator
										.getPatchSchema(oClass);
				break;
				case STRICT_PATCH:
					classSchema = 
						dispositionSchemaGenerator
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
