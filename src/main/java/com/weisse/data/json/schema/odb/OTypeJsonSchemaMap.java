package com.weisse.data.json.schema.odb;

import java.util.HashMap;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

/**
 * A map of functions useful to build JsonSchemas that represent all the types
 * allowed by OrientDB
 * @author weisse
 *
 */
public class OTypeJsonSchemaMap extends HashMap<OType,Function<OJsonSchemaConfiguration,JsonNode>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5965309922961226782L;
	
	/**
	 * The constructor builds the map according to the provided configuration
	 * @param configuration
	 */
	public OTypeJsonSchemaMap(){
		
		OTypeJsonSchemaMap self = this;
		
		ObjectNode anyObject = new ObjectNode(JsonNodeFactory.instance);
		Function<OJsonSchemaConfiguration,JsonNode> bareObjectFn = new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return anyObject;
			}
		};
		this.put(OType.EMBEDDED, bareObjectFn);
		this.put(OType.ANY, bareObjectFn);
		
		this.put(OType.LINK, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode linkObject = configuration.getLinkSchema();
				return linkObject;
			}
		});
		
		ObjectNode booleanObject = new ObjectNode(JsonNodeFactory.instance);
		booleanObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.BOOLEAN);
		this.put(OType.BOOLEAN, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return booleanObject;
			}
		});
		
		ObjectNode byteObject = new ObjectNode(JsonNodeFactory.instance);
		byteObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		byteObject.put(JsonSchemaDraft4.MINIMUM, Byte.MIN_VALUE);
		byteObject.put(JsonSchemaDraft4.MAXIMUM, Byte.MAX_VALUE);
		this.put(OType.BYTE, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return byteObject;
			}
		});
		
		ObjectNode binaryObject = new ObjectNode(JsonNodeFactory.instance);
		binaryObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		binaryObject.put(JsonSchemaDraft4.MINIMUM, 0);
		binaryObject.put(JsonSchemaDraft4.MAXIMUM, 2147483647);
		this.put(OType.BINARY, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return binaryObject;
			}
		});
		
		ObjectNode stringObject = new ObjectNode(JsonNodeFactory.instance);
		stringObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
		this.put(OType.STRING, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return stringObject;
			}
		});
		
		ObjectNode integerObject = new ObjectNode(JsonNodeFactory.instance);
		integerObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		integerObject.put(JsonSchemaDraft4.MINIMUM, Integer.MIN_VALUE);
		integerObject.put(JsonSchemaDraft4.MAXIMUM, Integer.MAX_VALUE);
		this.put(OType.INTEGER, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return integerObject;
			}
		});
		
		ObjectNode shortObject = new ObjectNode(JsonNodeFactory.instance);
		shortObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		shortObject.put(JsonSchemaDraft4.MINIMUM, Short.MIN_VALUE);
		shortObject.put(JsonSchemaDraft4.MAXIMUM, Short.MAX_VALUE);
		this.put(OType.SHORT, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return shortObject;
			}
		});
		
		ObjectNode longObject = new ObjectNode(JsonNodeFactory.instance);
		longObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		longObject.put(JsonSchemaDraft4.MINIMUM, Long.MIN_VALUE);
		longObject.put(JsonSchemaDraft4.MINIMUM, Long.MAX_VALUE);
		this.put(OType.LONG, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return longObject;
			}
		});
		
		ObjectNode floatObject = new ObjectNode(JsonNodeFactory.instance);
		floatObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		floatObject.put(JsonSchemaDraft4.MINIMUM, Float.MIN_VALUE);
		floatObject.put(JsonSchemaDraft4.MAXIMUM, Float.MAX_VALUE);
		this.put(OType.FLOAT, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return floatObject;
			}
		});

		ObjectNode doubleObject = new ObjectNode(JsonNodeFactory.instance);
		doubleObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		doubleObject.put(JsonSchemaDraft4.MINIMUM, Double.MIN_VALUE);
		doubleObject.put(JsonSchemaDraft4.MAXIMUM, Double.MAX_VALUE);
		this.put(OType.DOUBLE, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return doubleObject;
			}
		});

		ObjectNode decimalObject = new ObjectNode(JsonNodeFactory.instance);
		decimalObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		this.put(OType.DECIMAL, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				return decimalObject;
			}
		});
		
		this.put(OType.DATE, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode dateObject = new ObjectNode(JsonNodeFactory.instance);
				dateObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
				dateObject.put(JsonSchemaDraft4.PATTERN, configuration.getDatePattern());
				return dateObject;
			}
		});
		
		this.put(OType.DATETIME, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode dateTimeObject = new ObjectNode(JsonNodeFactory.instance);
				dateTimeObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
				dateTimeObject.put(JsonSchemaDraft4.PATTERN, configuration.getDateTimePattern());
				return dateTimeObject;
			}
		});
		
		this.put(OType.EMBEDDEDLIST, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode embeddedListObject = new ObjectNode(JsonNodeFactory.instance);
				embeddedListObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
				embeddedListObject.put(JsonSchemaDraft4.ITEMS, self.get(OType.ANY).apply(null));
				embeddedListObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
				return embeddedListObject;
			}
		});
		
		this.put(OType.EMBEDDEDSET, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode embeddedSetObject = new ObjectNode(JsonNodeFactory.instance);
				embeddedSetObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
				embeddedSetObject.put(JsonSchemaDraft4.ITEMS, self.get(OType.ANY).apply(null));
				embeddedSetObject.put(JsonSchemaDraft4.UNIQUE_ITEMS, true);
				embeddedSetObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
				return embeddedSetObject;
			}
		});
		
		this.put(OType.EMBEDDEDMAP, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode embeddedMapObject = new ObjectNode(JsonNodeFactory.instance);
				embeddedMapObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.OBJECT);
				embeddedMapObject.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, self.get(OType.ANY).apply(null));
				embeddedMapObject.put(JsonSchemaDraft4.MAX_PROPERTIES, 41000000);
				return embeddedMapObject;
			}
		});
		
		this.put(OType.LINKLIST, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode linkListObject = new ObjectNode(JsonNodeFactory.instance);
				linkListObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
				linkListObject.put(JsonSchemaDraft4.ITEMS, self.get(OType.LINK).apply(null));
				linkListObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
				return linkListObject;
			}
		});
		
		this.put(OType.LINKSET, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode linkSetObject = new ObjectNode(JsonNodeFactory.instance);
				linkSetObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
				linkSetObject.put(JsonSchemaDraft4.ITEMS, self.get(OType.LINK).apply(null));
				linkSetObject.put(JsonSchemaDraft4.UNIQUE_ITEMS, true);
				linkSetObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
				return linkSetObject;
			}
		});
		
		this.put(OType.LINKMAP, new Function<OJsonSchemaConfiguration,JsonNode>(){
			@Override
			public JsonNode apply(OJsonSchemaConfiguration configuration) {
				ObjectNode linkMapObject = new ObjectNode(JsonNodeFactory.instance);
				linkMapObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.OBJECT);
				linkMapObject.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, self.get(OType.LINK).apply(null));
				linkMapObject.put(JsonSchemaDraft4.MAX_PROPERTIES, 41000000);
				return null;
			}
		});
		
	}
	
}
