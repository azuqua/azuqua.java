import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.exceptions.ResumeIdIsNullException;
import com.azuqua.java.client.AzuquaResponse;
import com.azuqua.java.client.Flo;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    public void floInvokeTest() throws Exception {
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
     * Invokes a resume capable FLO where the invoke takes a json with property a, the resume takes a json with a property b
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ResumeIdIsNullException
     * @throws InterruptedException
     */
    @Test
    public void floResumeTest() throws NoSuchAlgorithmException, InvalidKeyException, IOException, ResumeIdIsNullException, InterruptedException {
        Azuqua azuqua = new Azuqua(key, secret, host, 443);
        Flo flo = azuqua.getFloInstance("resume", "90bac28b901371bc8a4ea3f7f2aa9d92");
        AzuquaResponse invokeResponse = flo.invoke("{\"a\":\"this is a very simple test\"}");
        TimeUnit.SECONDS.sleep(5);
        AzuquaResponse resumeResponse = flo.resume("{\"b\":\"resuming!\"}");

        assertTrue("resume capable flos should return {\"response\":\"success\"} " +
                "for a 200 response", invokeResponse.getResponse().equals("{\"response\":\"success\"}"));

        assertTrue(resumeResponse.getResponse().contains("this is a very simple test"));
        assertTrue(resumeResponse.getResponse().contains("resuming!"));


    }

    /**
     * TODO need data
     */
    @Test
    public void telemetryTest() {

    }
}
