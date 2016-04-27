package com.weisse.data.json.schema.odb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public final class OJsonSchema {

	private final ObjectNode schema;
	private final JsonSchema validator;
	private static final JsonSchemaFactory FACTORY = JsonSchemaFactory.newBuilder().freeze();
	
	public OJsonSchema(ObjectNode schema) throws ProcessingException{
		this.schema = schema;
		this.validator = FACTORY.getJsonSchema(this.schema);
	}
	
	/**
	 * It returns the ObjectNode that describes the schema
	 * @return
	 */
	public ObjectNode getSchema(){
		return this.schema;
	}
	
	/**
	 * It returns the validator of the schema
	 * @return
	 */
	public JsonSchema getValidator(){
		return this.validator;
	}
	
	/**
	 * It returns the schema in the form of a string
	 */
	public String toString(){
		return this.schema.toString();
	}
	
}
