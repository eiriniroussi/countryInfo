package service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import exception.countriesAPIException;
import model.countryInfo;
import countryAPI.CountryAPI;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class deserializeData {

		private String API_URL;
		
		public deserializeData(String API_URL) {
			this.API_URL = CountryAPI.getApiUrl();

		}
  
		    //Retrieve the list of all countries
		    //https://restcountries.com/v3.1/all?fields=name,capital,currencies,population,continents

			public countryInfo[] searchAllCountries(String apiType) throws countriesAPIException, URISyntaxException{
				countryInfo[] result = getAPIData(apiType,"","https://restcountries.com");
				for (countryInfo country : result) {
					
					System.out.print(country);;


				System.out.println("---------------------------");
				}
				return  result;
			}
			
			

			//Retrieve information about a country using the name of the country
			//https://restcountries.com/v3.1/name/greece?fields=name,capital,currencies,population,continents

			public  countryInfo[] getCountryByLang(String apiType, String language) throws countriesAPIException, URISyntaxException {
				
				try {
								countryInfo[] result = getAPIData(apiType,language,API_URL);
			    for (countryInfo country : result) {
					
					System.out.print(country);;


				System.out.println("---------------------------");
				}
				return result;
				}catch(countriesAPIException e) {
					throw new countriesAPIException("Inputs are incorrect!", e);
				}
				


			}
			
			
			
			
			//Retrieve information about a country using the name of the country
			//https://restcountries.com/v3.1/name/greece?fields=name,capital,currencies,population,continents

			public countryInfo[] getCountryByName(String apiType, String name) throws countriesAPIException, URISyntaxException {
					
				
				try {
					countryInfo[] result = getAPIData(apiType,name,API_URL);
				
					for (countryInfo country : result) {
						
						System.out.print(country);;
						System.out.println("---------------------------");
					}
					return result;
				}catch(countriesAPIException e) {
						throw new countriesAPIException("Inputs are incorrect!", e);
				}
			}
			
			
			
			
			//Find all the countries that have a selected currency
			//https://restcountries.com/v3.1/currency/euro?fields=name,capital,currencies,population,continents

			public countryInfo[] getCountryByCurrency(String apiType, String currency) throws countriesAPIException, URISyntaxException {
					
				
				try {
					countryInfo[] result = getAPIData(apiType,currency,API_URL);
				
					for (countryInfo country : result) {
						
						System.out.print(country);;
						System.out.println("---------------------------");
					}
					return result;
				}catch(countriesAPIException e) {
						throw new countriesAPIException("Inputs are incorrect!", e);
				}
			}

			
			
			//URL builder and request
			
			private countryInfo[] getAPIData(String apiType, String apiSelection, String API_URL) throws countriesAPIException, URISyntaxException {
			
				try{
					
					//Build the URL
					final URIBuilder uriBuilder = new URIBuilder(API_URL).setPathSegments("v3.1", apiType, apiSelection).addParameter("fields", "name,capital,currencies,population,continents");
		
				
			        if (apiType != null) {
						switch (apiType) {
						case "currency":
							uriBuilder.addParameter("currency", apiSelection);
							break;
						case "lang":
							uriBuilder.addParameter("lang", apiSelection);
							break;
						case "name":
							uriBuilder.addParameter("name", apiSelection);
						}
					}else {
						uriBuilder.addParameter("all", apiSelection);
					}
			        
					final URI urlAPI = uriBuilder.build();
					//System.out.print(urlAPI);TESTING
					
					//GET method retrieving data
					final HttpGet getRequest = new HttpGet(urlAPI);
					
					//Create a new HTTP Client
					final CloseableHttpClient httpClient = HttpClients.createDefault();
					
					try(CloseableHttpResponse httpResponse = httpClient.execute(getRequest)) {
						final HttpEntity entity = httpResponse.getEntity();
						//String jsonResponse = httpClient.execute(getRequest, httpResponse -> EntityUtils.toString(httpResponse.getEntity())).toString();
						ObjectMapper mapper = new ObjectMapper();
						int statusCode = httpResponse.getStatusLine().getStatusCode();
						if (statusCode != HttpStatus.SC_OK) {
							String errorMessage = EntityUtils.toString(entity);
			                System.out.println("Error: " + errorMessage); // Log the error message
			                throw new countriesAPIException("API response error: " + errorMessage);
						}
						 return  mapper.readValue(entity.getContent(), countryInfo[].class);
						
					}catch(IOException e) {
						throw new countriesAPIException("Error requesting data from the API", e);
					}
					
				} catch (URISyntaxException e) {
					throw new countriesAPIException("Unable to create request URI.", e);
				}
			}
	}
					
					

			
			
			


 
