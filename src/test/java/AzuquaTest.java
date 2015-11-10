import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.exceptions.ResumeIdIsNullException;
import com.azuqua.java.client.AzuquaResponse;
import com.azuqua.java.client.Flo;
import com.google.gson.JsonObject;
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

        for (Flo flo : flos) {
            if (flo.getAlias().equals("9d88f3f06814482eafa7a411fb12199c")) {
                JsonObject data = new JsonObject();
                data.addProperty("a", "this is a very simple test");
                AzuquaResponse response = flo.invoke(data.toString());

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
    }

    /**
     * <p>
     *     Invokes a resume capable FLO where the invoke takes "a" json with
     *     property a, the resume takes a json with a property "b"
     * </p>
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws ResumeIdIsNullException
     * @throws InterruptedException
     */
    @Test
    public void floResumeTest() throws NoSuchAlgorithmException, InvalidKeyException, IOException, ResumeIdIsNullException, InterruptedException {
        Azuqua azuqua = new Azuqua(key, secret, host, 443);

        // Azuqua#getFloInstance accepts the flo name (can be any name) and the FLO alias.
        Flo flo = azuqua.getFloInstance("resume", "90bac28b901371bc8a4ea3f7f2aa9d92");
        JsonObject invokeData = new JsonObject();
        invokeData.addProperty("a", "this is a very simple test");
        AzuquaResponse invokeResponse = flo.invoke(invokeData.toString());
        TimeUnit.SECONDS.sleep(5);

        JsonObject resumeData =  new JsonObject();
        resumeData.addProperty("b", "resuming!");
        AzuquaResponse resumeResponse = flo.resume(resumeData.toString());

        assertTrue("resume capable flos should return {\"response\":\"success\"} " +
                "for a 200 response", invokeResponse.getResponse().contains("success"));

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
