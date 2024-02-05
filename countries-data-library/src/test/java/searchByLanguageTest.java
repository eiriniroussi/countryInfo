import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.junit.Test;

import exception.countriesAPIException;
import model.countryInfo;
import service.deserializeData;

public class searchByLanguageTest {

	@Test
	public void testSearchByLanguage() throws URISyntaxException {
        deserializeData deserializer = new deserializeData("https://restcountries.com");
        try {
            countryInfo[] countries = null;
			countries = deserializer.getCountryByLang("lang","greek");
            assertNotNull("Countries list should not be null", countries);
        } catch (countriesAPIException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        } catch (RuntimeException e) {
            fail("Runtime exception should not be thrown: " + e.getMessage());
        }

	}}
