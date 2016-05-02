package com.weisse.data.json.schema.odb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.weisse.data.json.schema.odb.interfaces.PropertyNameStrategy;
import com.weisse.data.json.schema.odb.interfaces.RequiredPropertyStrategy;
import com.weisse.data.json.schema.odb.strategy.NameAndFullNameAliases;
import com.weisse.data.json.schema.odb.strategy.RequiredEdgesEnds;
import com.weisse.data.json.schema.odb.vocabulary.JsonSchemaDraft4;

public class OJsonSchemaConfiguration {
	
	private String schemaSPECS = "http://json-schema.org/draft-04/schema#";
	private URL baseURL;
	private String datePattern;
	private String dateTimePattern;
	private ObjectNode linkSchema;
	private final Set<String> required = new HashSet<String>();
	private final Set<String> unrequired = new HashSet<String>();
	private final Map<String,String> aliases = new HashMap<String,String>();
	private PropertyNameStrategy propertyNameStrategy;
	private RequiredPropertyStrategy requiredPropertyStrategy;
	
	public OJsonSchemaConfiguration(){
		this.setDefaults();
	}
	
	private void setDefaults(){
		URL url;
		try {
			url = new URL("http", "localhost", 2470, "");
			this.setBaseURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.setDatePattern("^\\d\\d*?-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
		this.setDateTimePattern("^\\d\\d*?-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\s(0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]|60)(:(0[1-9]|[1-5][0-9]|60),\\d\\d*$|:(0[1-9]|[1-5][0-9]|60)$)");
		ObjectNode linkSchemaObject = new ObjectNode(JsonNodeFactory.instance);
		linkSchemaObject.put(JsonSchemaDraft4.TYPE, JsonSchemaDraft4.STRING);
		this.setLinkSchema(linkSchemaObject);
		this.setPropertyNameStrategy(NameAndFullNameAliases.getInstance());
		this.setRequiredPropertyStrategy(RequiredEdgesEnds.getInstance());
	}

	public RequiredPropertyStrategy getRequiredPropertyStrategy() {
		return requiredPropertyStrategy;
	}

	public void setRequiredPropertyStrategy(
			RequiredPropertyStrategy requiredPropertyStrategy) {
		this.requiredPropertyStrategy = requiredPropertyStrategy;
	}

	public PropertyNameStrategy getPropertyNameStrategy() {
		return propertyNameStrategy;
	}

	public void setPropertyNameStrategy(PropertyNameStrategy propertyNameStrategy) {
		this.propertyNameStrategy = propertyNameStrategy;
	}

	public String getSchemaSPECS() {
		return this.schemaSPECS;
	}

	public URL getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(URL baseUrl) {
		this.baseURL = baseUrl;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public ObjectNode getLinkSchema() {
		return linkSchema;
	}

	public void setLinkSchema(ObjectNode linkSchema) {
		this.linkSchema = linkSchema;
	}
	
	public Set<String> getRequired() {
		return required;
	}

	public Set<String> getUnrequired() {
		return unrequired;
	}
	
	public Map<String,String> getAliases() {
		return aliases;
	}
	
}
