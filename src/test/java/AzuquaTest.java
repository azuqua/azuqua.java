import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.model.AzuquaResponse;
import com.azuqua.java.client.model.Flo;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by quyle on 10/30/15.
 */
public class AzuquaTest {
    String key = System.getenv().get("ALPHA_KEY");
    String secret =  System.getenv().get("ALPHA_SECRET");
    String host = System.getenv().get("ALPHA_HOST");

    @Test
    public void createAzuquaInstanceTest() {
        Azuqua azuqua = new Azuqua(key, secret);
        assertEquals(key, azuqua.getAccessKey());
        assertEquals(secret, azuqua.getAccessSecret());
    }

    @Test
    public void getFlosTest() throws Exception {
        assertNotNull(host);
        Azuqua azuqua = new Azuqua(key, secret, host, 443);
        assertEquals(host, azuqua.getHost());
        assertEquals(443, azuqua.getPort());

        Collection<Flo> flos = azuqua.getFlos();
        assertEquals("I only have one test flo in my account.", 1, flos.size());

        for (Flo flo : flos) {
            AzuquaResponse response = flo.invoke("{\"a\":\"this is a very simple test\"}");

            // test for valid uuid for x-flo-instance
            assertNotNull("xFloInstance property should not be null", response.getXFloInstance());
            assertTrue("xFloInstance property length should be greater than 1 " +
                    "since it's generated via uuid.v4.", response.getXFloInstance().length() > 1);
            UUID uuid = UUID.fromString(response.getXFloInstance());
            assertEquals("generated uuid should equal the response uuid", response.getXFloInstance(), uuid.toString());

            // test the response body contains whatever we passed it
            assertTrue(response.getResponse().contains("this is a very simple test"));
        }
    }

    /**
     * TODO need data
     */
    @Test
    public void telemetryTest() {

    }
}
