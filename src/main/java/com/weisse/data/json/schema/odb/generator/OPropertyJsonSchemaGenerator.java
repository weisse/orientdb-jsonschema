package com.weisse.data.json.schema.odb.generator;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.weisse.data.json.schema.odb.OJsonSchemaConfiguration;
import com.weisse.data.json.schema.odb.interfaces.PropertyConstraint;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;


/**
 * This class is able to generate or export JsonSchema starting from a OProperty
 * @author weisse
 *
 */
public class OPropertyJsonSchemaGenerator {

	private OJsonSchemaConfiguration configuration;
	private final Collection<PropertyConstraint> constraints = new HashSet<PropertyConstraint>();
	
	/**
	 * The constructor fills the constraint collection passing them
	 * the provided configuration
	 * @param configuration
	 */
	public OPropertyJsonSchemaGenerator(OJsonSchemaConfiguration configuration){
		this.configuration = configuration;
		fillConstraintCollection();
	}
	
	/**
	 * It fills the collection of constraints to apply on property schemas
	 */
	private void fillConstraintCollection(){
		FastClasspathScanner scanner = 
				new FastClasspathScanner("com.weisse.data.json.schema.odb.constraint");
		scanner.matchClassesImplementing(PropertyConstraint.class, subclass -> {
			try {				
				Constructor<? extends PropertyConstraint> constructor =
						(Constructor<? extends PropertyConstraint>)
						subclass.getConstructor(OJsonSchemaConfiguration.class);
				this.constraints.add(constructor.newInstance(this.configuration));
			} catch (Exception e) {}
		}).scan();
	}
	
	/**
	 * It prepares the property JsonSchema object
	 * @return
	 */
	private ObjectNode prepareSchemaObject(){
		ObjectNode propertySchema = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode jsonOneOf = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonAllOfWrapper = new ObjectNode(JsonNodeFactory.instance);
		ArrayNode jsonAllOf = new ArrayNode(JsonNodeFactory.instance);
		jsonAllOfWrapper.put(JsonSchemaDraft4.ALL_OF, jsonAllOf);
		jsonOneOf.add(jsonAllOfWrapper);
		propertySchema.put(JsonSchemaDraft4.ONE_OF, jsonOneOf);
		return propertySchema;
	}
	
	/**
	 * It returns the JsonSchema that validates the single provided property
	 * @param oProperty
	 * @return
	 */
	public ObjectNode getSchema(OProperty oProperty){
		ObjectNode propertySchema = this.prepareSchemaObject();
		for(PropertyConstraint constraint: this.constraints){
			constraint.apply(oProperty, propertySchema, false);
		}
		return propertySchema;
	}
	
	/**
	 * It returns the JsonSchema that validates the single provided property
	 * filled with references instead of plain schema
	 * @param oProperty
	 * @return
	 */
	public ObjectNode exportSchema(OProperty oProperty){
		ObjectNode propertySchema = this.prepareSchemaObject();
		for(PropertyConstraint constraint: this.constraints){
			constraint.apply(oProperty, propertySchema, true);
		}
		return propertySchema;
	}
	
}
