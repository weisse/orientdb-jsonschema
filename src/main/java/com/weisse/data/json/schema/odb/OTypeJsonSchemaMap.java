package com.weisse.data.json.schema.odb;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OType;

public class OTypeJsonSchemaMap extends HashMap<OType,JsonNode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5965309922961226782L;
	
	private static OTypeJsonSchemaMap INSTANCE;
	
	static{
		INSTANCE = new OTypeJsonSchemaMap();
	}
	
	public static OTypeJsonSchemaMap getInstance(){
		return INSTANCE;
	}
	
	public static void refresh(){
		INSTANCE = new OTypeJsonSchemaMap();
	}
	
	private OTypeJsonSchemaMap(){
		
		OJsonSchemaConfiguration configuration = OJsonSchemaConfiguration.getInstance();
		
		ObjectNode anyObject = new ObjectNode(JsonNodeFactory.instance);
		this.put(OType.EMBEDDED, anyObject);
		this.put(OType.ANY, anyObject);
		
		ObjectNode linkObject = configuration.getLinkSchema();
		this.put(OType.LINK, linkObject);
		
		ObjectNode booleanObject = new ObjectNode(JsonNodeFactory.instance);
		booleanObject.put("type", "boolean");
		this.put(OType.BOOLEAN, booleanObject);
		
		ObjectNode byteObject = new ObjectNode(JsonNodeFactory.instance);
		byteObject.put("type", "number");
		byteObject.put("minimum", -128);
		byteObject.put("maximum", 127);
		this.put(OType.BYTE, byteObject);
		
		ObjectNode binaryObject = new ObjectNode(JsonNodeFactory.instance);
		binaryObject.put("type", "number");
		binaryObject.put("minimum", 0);
		binaryObject.put("maximum", 2147483647);
		this.put(OType.BINARY, binaryObject);
		
		ObjectNode stringObject = new ObjectNode(JsonNodeFactory.instance);
		stringObject.put("type", "string");
		this.put(OType.STRING, stringObject);
		
		ObjectNode integerObject = new ObjectNode(JsonNodeFactory.instance);
		integerObject.put("type", "number");
		integerObject.put("minimum", -2147483648);
		integerObject.put("maximum", 2147483647);
		this.put(OType.INTEGER, integerObject);
		
		ObjectNode shortObject = new ObjectNode(JsonNodeFactory.instance);
		shortObject.put("type", "number");
		shortObject.put("minimum", -32768);
		shortObject.put("maximum", 32767);
		this.put(OType.SHORT, shortObject);
		
		ObjectNode longObject = new ObjectNode(JsonNodeFactory.instance);
		longObject.put("type", "number");
		longObject.put("minimum", -2e63);
		longObject.put("minimum", 2e63-1);
		this.put(OType.LONG, longObject);
		
		ObjectNode floatObject = new ObjectNode(JsonNodeFactory.instance);
		floatObject.put("type", "number");
		floatObject.put("minimum", 2e-149);
		floatObject.put("maximum", (2-(2e-23))*(2e127));
		this.put(OType.FLOAT, floatObject);
		
		// TODO: fix the problem of dobule numbers range
		ObjectNode doubleObject = new ObjectNode(JsonNodeFactory.instance);
		doubleObject.put("type", "number");
		doubleObject.put("minimum", "2e-1074");
		doubleObject.put("maximum", "(2-(2e-52))*(2e1023)");
		this.put(OType.DOUBLE, doubleObject);

		ObjectNode decimalObject = new ObjectNode(JsonNodeFactory.instance);
		decimalObject.put("type", "number");
		this.put(OType.DECIMAL, decimalObject);
		
		ObjectNode dateObject = new ObjectNode(JsonNodeFactory.instance);
		dateObject.put("type", "string");
		dateObject.put("pattern", configuration.getDatePattern());
		this.put(OType.DATE, dateObject);
		
		ObjectNode dateTimeObject = new ObjectNode(JsonNodeFactory.instance);
		dateTimeObject.put("type", "string");
		dateTimeObject.put("pattern", configuration.getDateTimePattern());
		this.put(OType.DATETIME, dateTimeObject);
		
		ObjectNode embeddedListObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedListObject.put("type", "array");
		embeddedListObject.put("items", this.get(OType.ANY));
		embeddedListObject.put("maxItems", 41000000);
		this.put(OType.EMBEDDEDLIST, embeddedListObject);
		
		ObjectNode embeddedSetObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedSetObject.put("type", "array");
		embeddedSetObject.put("items", this.get(OType.ANY));
		embeddedSetObject.put("uniqueItems", true);
		embeddedSetObject.put("maxItems", 41000000);
		this.put(OType.EMBEDDEDSET, embeddedSetObject);
		
		ObjectNode embeddedMapObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedMapObject.put("type", "object");
		embeddedMapObject.put("additionalProperties", this.get(OType.ANY));
		embeddedMapObject.put("maxProperties", 41000000);
		this.put(OType.EMBEDDEDMAP, embeddedMapObject);
		
		ObjectNode linkListObject = new ObjectNode(JsonNodeFactory.instance);
		linkListObject.put("type", "array");
		linkListObject.put("items", this.get(OType.LINK));
		linkListObject.put("maxItems", 41000000);
		this.put(OType.LINKLIST, linkListObject);
		
		ObjectNode linkSetObject = new ObjectNode(JsonNodeFactory.instance);
		linkSetObject.put("type", "array");
		linkSetObject.put("items", this.get(OType.LINK));
		linkSetObject.put("uniqueItems", true);
		linkSetObject.put("maxItems", 41000000);
		this.put(OType.LINKSET, linkSetObject);
		
		ObjectNode linkMapObject = new ObjectNode(JsonNodeFactory.instance);
		linkMapObject.put("type", "object");
		linkMapObject.put("additionalProperties", this.get(OType.LINK));
		linkMapObject.put("maxProperties", 41000000);
		this.put(OType.LINKMAP, linkMapObject);
		
	}
	
}
