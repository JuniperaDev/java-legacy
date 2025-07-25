package com.neopragma.legacy.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CityStateLookupImpl implements CityStateLookup {

	@Override
	public CityState lookup(String zipCode) throws URISyntaxException, ClientProtocolException, IOException {
		// Use zippopotam.us API to look up the city and state based on zip code.
		String city = "";
		String state = "";
		try {
			URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("api.zippopotam.us")
				.setPath("/us/" + zipCode.substring(0,5))
				.build();
			HttpGet request = new HttpGet(uri);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse response = httpclient.execute(request);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					JsonElement jelement = new JsonParser().parse(result.toString());
					JsonObject jobject = jelement.getAsJsonObject();
					JsonArray jarray = jobject.getAsJsonArray("places");
					jobject = jarray.get(0).getAsJsonObject();
					city = jobject.get("place name").getAsString();
					state = jobject.get("state abbreviation").getAsString();
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			// Return empty values if lookup fails
		}
		return new CityState(city, state);
	}
}
