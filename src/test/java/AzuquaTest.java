import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaResponse;
import com.azuqua.java.client.exceptions.ResumeIdIsNullException;
import com.azuqua.java.client.FloResponse;
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
    String alpha_key = System.getenv().get("ALPHA_KEY");
    String alpha_secret =  System.getenv().get("ALPHA_SECRET");
    String alpha_host = System.getenv().get("ALPHA_HOST");

    String beta_key = System.getenv().get("BETA_KEY");
    String beta_secret = System.getenv().get("BETA_SECRET");
    String beta_host = System.getenv().get("BETA_HOST");
    String beta_error_flo = System.getenv().get("BETA_ERROR_FLO");


    @Test
    public void createAzuquaInstanceTest() {
        Azuqua azuqua = new Azuqua(alpha_key, alpha_secret);
        assertEquals(alpha_key, azuqua.getAccessKey());
        assertEquals(alpha_secret, azuqua.getAccessSecret());
    }

    @Test
    public void floInvokeTest() throws Exception {
        FloResponse response = null;
        Azuqua azuqua = new Azuqua(alpha_key, alpha_secret, alpha_host, 443);
        Collection<Flo> flos = azuqua.getFlos();
        String message = "this is a very simple test";

        for (Flo flo : flos) {
            if (flo.getAlias().equals("9d88f3f06814482eafa7a411fb12199c")) {
                JsonObject data = new JsonObject();
                data.addProperty("a", message);
                response = flo.invoke(data.toString());
            }
        }

        // tests the azuqua object
        assertNotNull(alpha_host);
        assertEquals(alpha_host, azuqua.getHost());
        assertEquals(443, azuqua.getPort());

        // tests the response coming back
        assertNotNull(response);
        assertNotNull("xFloInstance property should not be null", response.getXFloInstance());
        assertTrue("xFloInstance property length should be greater than 1 " +
                "since it's generated via uuid.v4.", response.getXFloInstance().length() > 1);
        UUID uuid = UUID.fromString(response.getXFloInstance());
        assertEquals("generated uuid should equal the response uuid", response.getXFloInstance(), uuid.toString());

        // test the response body contains whatever we passed it
        JsonObject data = response.getData();
        assertEquals("response.body.data.a should contain the message", message, data.get("a").getAsString());
    }

    /**
     * <p>
     *     This flo will throws an error.
     * </p>
     *
     * <p>
     *     Format of the error message in the body looks like this:
     *
     *     {
     *         "statusCode": "some integer value greater or equal to 400",
     *         "message": "the error message coming back either from the Azuqua Engine or the API"
     *     }
     *
     *     The assertions imply what the format should be for this case.
     *
     *     If FloResponse#statusCode is null, that implies that there isn't an error.
     * </p>
     *
     *
     * @throws Exception
     */
    @Test
    public void floInvokeErrorTest() throws Exception {
        // uses beta creds
        Azuqua azuqua = new Azuqua(beta_key, beta_secret, beta_host, 443);
        Flo flo = azuqua.getFloInstance(beta_error_flo);

        JsonObject data = new JsonObject();
        data.addProperty("patient_id", "1");
        data.addProperty("ma_id", "1");
        FloResponse response = flo.invoke(data.toString());

        assertEquals("should be a 400 status code", new Integer(400), response.getStatusCode());
        assertNotNull("message body should contain some value", response.getMessage());

        assertNull("should be null", response.getData());
        assertNull("should be null", response.getResponse());
        assertNotNull("should be null", response.getXFloInstance());
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
        Azuqua azuqua = new Azuqua(alpha_key, alpha_secret, alpha_host, 443);

        // Azuqua#getFloInstance accepts the flo name (can be any name) and the FLO alias.
        Flo flo = azuqua.getFloInstance("resume", "90bac28b901371bc8a4ea3f7f2aa9d92");
        JsonObject invokeData = new JsonObject();
        invokeData.addProperty("a", "this is a very simple test");
        FloResponse invokeResponse = flo.invoke(invokeData.toString());
        TimeUnit.SECONDS.sleep(2);

        JsonObject resumeData =  new JsonObject();
        resumeData.addProperty("b", "resuming!");
        FloResponse resumeResponse = flo.resume(resumeData.toString());

        assertTrue("resume capable flos should return {\"response\":\"success\"} " +
                "for a 200 response", invokeResponse.getResponse().contains("success"));

        assertTrue("response from the resume should be a { data: { a: 'this is a very simple test' , b: 'resuming!' } }",
                resumeResponse.getData().get("a").getAsString().equals("this is a very simple test"));
        assertTrue(resumeResponse.getData().get("b").getAsString().equals("resuming!"));
    }

    /**
     * TODO need data
     */
    @Test
    public void telemetryTest() {

    }
}
