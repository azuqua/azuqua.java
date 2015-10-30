import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaException;
import com.azuqua.java.client.model.Flo;
import lombok.extern.java.Log;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by quyle on 10/30/15.
 */
@Log
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
    public void getFlosTest() throws AzuquaException {
        assertNotNull(host);
        Azuqua azuqua = new Azuqua(key, secret, host, 443);
        assertEquals(host, azuqua.getHost());
        assertEquals(443, azuqua.getPort());

        Collection<Flo> flos = azuqua.getFlos();
        assertEquals("I only have one test flo in my account.", 1, flos.size());
        for (Flo flo : flos) {
            String response = flo.invoke("{\"a\":\"this is a very simple test\"}");
            assertTrue(response.contains("this is a very simple test"));
        }
    }

    /**
     * TODO need data
     */
    @Test
    public void telemetryTest() {

    }
}
