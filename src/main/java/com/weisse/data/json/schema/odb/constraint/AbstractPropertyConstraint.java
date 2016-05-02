package com.weisse.data.json.schema.odb.constraint;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.OTypeJsonSchemaMap;
import com.weisse.data.json.schema.odb.generator.OClassJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.generator.OPropertyJsonSchemaGenerator;
import com.weisse.data.json.schema.odb.interfaces.PropertyConstraint;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

/**
 * This class represents the abstraction of a PropertyConstraint
 * 
 * It provides some utilities such as the Lazy initialization of
 * generators
 * 
 * @author weisse
 *
 */
public abstract class AbstractPropertyConstraint implements PropertyConstraint {
	
	OJsonSchemaConfiguration configuration;
	OTypeJsonSchemaMap typeMap;
	OClassJsonSchemaGenerator classSchemaGenerator;
	OPropertyJsonSchemaGenerator propertySchemaGenerator;
	
	public AbstractPropertyConstraint(OJsonSchemaConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Lazy initialization of a {@link OTypeJsonSchemaMap}
	 * @return
	 */
	protected OTypeJsonSchemaMap getTypeMap(){
		if(this.typeMap == null){
			this.typeMap = new OTypeJsonSchemaMap();
		}
		return this.typeMap;
	}
	
	/**
	 * Lazy initialization of {@link OClassJsonSchemaGenerator}
	 * @return
	 */
	protected OClassJsonSchemaGenerator getOClassSchemaGenerator(){
		if(this.classSchemaGenerator == null){
			this.classSchemaGenerator = 
					new OClassJsonSchemaGenerator(this.configuration);
		}
		return this.classSchemaGenerator;
	}
	
	/**
	 * Lazy initialization of {@link OPropertyJsonSchemaGenerator}
	 * @return
	 */
	protected OPropertyJsonSchemaGenerator getOPropertySchemaGenerator(){
		if(this.propertySchemaGenerator == null){
			this.propertySchemaGenerator = 
					new OPropertyJsonSchemaGenerator(this.configuration);
		}
		return this.propertySchemaGenerator;
	}
	
	/**
	 * It returns the part of JsonSchema where alternative to standard
	 * JsonSchema validator of a property are listed
	 * @param propertySchema
	 * @return
	 */
	protected ArrayNode getAlternatives(ObjectNode propertySchema){
		return (ArrayNode) propertySchema.get(JsonSchemaDraft4.ONE_OF);
	}
	
	/**
	 * It returns the part of JsonSchema where constraints are listed
	 * @param propertySchema
	 * @return
	 */
	protected ArrayNode getConstraints(ObjectNode propertySchema){
		return (ArrayNode) this.getAlternatives(propertySchema).get(0).get(JsonSchemaDraft4.ALL_OF);
	}

}
