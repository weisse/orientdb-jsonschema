package com.weisse.data.json.schema.odb;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

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
		booleanObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.BOOLEAN);
		this.put(OType.BOOLEAN, booleanObject);
		
		ObjectNode byteObject = new ObjectNode(JsonNodeFactory.instance);
		byteObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		byteObject.put(JsonSchemaDraft4.MINIMUM, -128);
		byteObject.put(JsonSchemaDraft4.MAXIMUM, 127);
		this.put(OType.BYTE, byteObject);
		
		ObjectNode binaryObject = new ObjectNode(JsonNodeFactory.instance);
		binaryObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		binaryObject.put(JsonSchemaDraft4.MINIMUM, 0);
		binaryObject.put(JsonSchemaDraft4.MAXIMUM, 2147483647);
		this.put(OType.BINARY, binaryObject);
		
		ObjectNode stringObject = new ObjectNode(JsonNodeFactory.instance);
		stringObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
		this.put(OType.STRING, stringObject);
		
		ObjectNode integerObject = new ObjectNode(JsonNodeFactory.instance);
		integerObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		integerObject.put(JsonSchemaDraft4.MINIMUM, -2147483648);
		integerObject.put(JsonSchemaDraft4.MAXIMUM, 2147483647);
		this.put(OType.INTEGER, integerObject);
		
		ObjectNode shortObject = new ObjectNode(JsonNodeFactory.instance);
		shortObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		shortObject.put(JsonSchemaDraft4.MINIMUM, -32768);
		shortObject.put(JsonSchemaDraft4.MAXIMUM, 32767);
		this.put(OType.SHORT, shortObject);
		
		ObjectNode longObject = new ObjectNode(JsonNodeFactory.instance);
		longObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		longObject.put(JsonSchemaDraft4.MINIMUM, -2e63);
		longObject.put(JsonSchemaDraft4.MINIMUM, 2e63-1);
		this.put(OType.LONG, longObject);
		
		ObjectNode floatObject = new ObjectNode(JsonNodeFactory.instance);
		floatObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		floatObject.put(JsonSchemaDraft4.MINIMUM, 2e-149);
		floatObject.put(JsonSchemaDraft4.MAXIMUM, (2-(2e-23))*(2e127));
		this.put(OType.FLOAT, floatObject);
		
		// TODO: fix the problem of dobule numbers range
		ObjectNode doubleObject = new ObjectNode(JsonNodeFactory.instance);
		doubleObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		doubleObject.put(JsonSchemaDraft4.MINIMUM, "2e-1074");
		doubleObject.put(JsonSchemaDraft4.MAXIMUM, "(2-(2e-52))*(2e1023)");
		this.put(OType.DOUBLE, doubleObject);

		ObjectNode decimalObject = new ObjectNode(JsonNodeFactory.instance);
		decimalObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.NUMBER);
		this.put(OType.DECIMAL, decimalObject);
		
		ObjectNode dateObject = new ObjectNode(JsonNodeFactory.instance);
		dateObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
		dateObject.put(JsonSchemaDraft4.PATTERN, configuration.getDatePattern());
		this.put(OType.DATE, dateObject);
		
		ObjectNode dateTimeObject = new ObjectNode(JsonNodeFactory.instance);
		dateTimeObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
		dateTimeObject.put(JsonSchemaDraft4.PATTERN, configuration.getDateTimePattern());
		this.put(OType.DATETIME, dateTimeObject);
		
		ObjectNode embeddedListObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedListObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
		embeddedListObject.put(JsonSchemaDraft4.ITEMS, this.get(OType.ANY));
		embeddedListObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
		this.put(OType.EMBEDDEDLIST, embeddedListObject);
		
		ObjectNode embeddedSetObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedSetObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
		embeddedSetObject.put(JsonSchemaDraft4.ITEMS, this.get(OType.ANY));
		embeddedSetObject.put(JsonSchemaDraft4.UNIQUE_ITEMS, true);
		embeddedSetObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
		this.put(OType.EMBEDDEDSET, embeddedSetObject);
		
		ObjectNode embeddedMapObject = new ObjectNode(JsonNodeFactory.instance);
		embeddedMapObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.OBJECT);
		embeddedMapObject.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, this.get(OType.ANY));
		embeddedMapObject.put(JsonSchemaDraft4.MAX_PROPERTIES, 41000000);
		this.put(OType.EMBEDDEDMAP, embeddedMapObject);
		
		ObjectNode linkListObject = new ObjectNode(JsonNodeFactory.instance);
		linkListObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
		linkListObject.put(JsonSchemaDraft4.ITEMS, this.get(OType.LINK));
		linkListObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
		this.put(OType.LINKLIST, linkListObject);
		
		ObjectNode linkSetObject = new ObjectNode(JsonNodeFactory.instance);
		linkSetObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.ARRAY);
		linkSetObject.put(JsonSchemaDraft4.ITEMS, this.get(OType.LINK));
		linkSetObject.put(JsonSchemaDraft4.UNIQUE_ITEMS, true);
		linkSetObject.put(JsonSchemaDraft4.MAX_ITEMS, 41000000);
		this.put(OType.LINKSET, linkSetObject);
		
		ObjectNode linkMapObject = new ObjectNode(JsonNodeFactory.instance);
		linkMapObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.OBJECT);
		linkMapObject.put(JsonSchemaDraft4.ADDITIONAL_PROPERTIES, this.get(OType.LINK));
		linkMapObject.put(JsonSchemaDraft4.MAX_PROPERTIES, 41000000);
		this.put(OType.LINKMAP, linkMapObject);
		
	}
	
}
