package com.azuqua.java.client;
import com.azuqua.java.client.exceptions.ResumeIdIsNullException;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *     Represents an Azuqua FLO.
 * </p>
 *
 * @author quyle
 */
public class Flo {
	private static Gson gson = new Gson();

	/**
	 * The name of the FLO.
	 */
	private String name;

	/**
	 * The alias of the FLO.
	 */
	private String alias;

    /**
     * The x-flo-instance header.
     */
    private String xFloInstance;

	/**
	 * Reference to the Azuqua object. Needed for the access key|secret and also for making requests to Azuqua.
	 */
	private Azuqua azuqua;

	protected Flo(String name, String alias){
		this.name = name;
		this.alias = alias;
	}

    /**
     * Resumes a Flo. Throws a ResumeIdIsNullException if the resume id is null.
     *
     * @param json
     * @return An FloResponse object.
     */
    public FloResponse resume(String json) throws InvalidKeyException, NoSuchAlgorithmException, IOException, ResumeIdIsNullException {
        if (this.xFloInstance == null) {
            throw new ResumeIdIsNullException();
        }

        String path = this.azuqua.resumeRoute.replace(":exec", this.xFloInstance);
        AzuquaResponse out = azuqua.makeRequest("POST", path, json);

        // can't call flo resume twice on the same id so set it to null for next go around
        this.xFloInstance = null;

        return gson.fromJson(out.getBody(), FloResponse.class);
    }

    /**
     * Resumes a Flo via the passed in parameter resumeId. Throws a ResumeIdIsNullException if the resume id is null.
     *
     * @param resumeId The flo resume id.
     * @param json The serialized json.
     * @return An FloResponse object.
     */
    public FloResponse resume(String resumeId, String json) throws ResumeIdIsNullException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (resumeId == null) {
            throw new ResumeIdIsNullException();
        }

        String path = this.azuqua.resumeRoute.replace(":exec", resumeId);
        AzuquaResponse out = azuqua.makeRequest("POST", path, json);

        // can't call flo resume twice on the same id so set it to null for next go around
        this.xFloInstance = null;
		return gson.fromJson(out.getBody(), FloResponse.class);
    }

	/**
     * <p>
     *     Invokes a flo.
     * </p>
	 *
     *
     * * <p>
     *     Sets the flo resume id if the FLO that has flo-resume capabilities.
     *     When the Flo#invoke method is called, the resume id will be saved as an instance
     *     property. This id will be use when the Flo#resume method is called.
     * </p>
	 * 
	 * @param json A serialized json.
	 * @return
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public FloResponse invoke(String json) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.invokeRoute.replace(":id", this.alias);

		AzuquaResponse out = azuqua.makeRequest("POST", path, json);
		this.xFloInstance = out.getXFloInstance();

		FloResponse response = gson.fromJson(out.getBody(), FloResponse.class);
		response.setXFloInstance(this.xFloInstance);
        return response;
	}

	/**
	 *
	 * @param execId The exec id of the flo.
	 * @return FloResponse object.
	 * @throws IOException There was a problem establishing a connection to the Azuqua server.
	 * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
	 * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public AzuquaResponse telemetryData(String execId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.telemetryData.replace(":alias", this.alias).replace(":exec", execId);
		AzuquaResponse out = azuqua.makeRequest("GET", path, "");
		return out;
	}

	/**
	 *
	 * @param execId The exec id of the flo.
	 * @return FloResponse object
	 * @throws IOException There was a problem establishing a connection to the Azuqua server.
	 * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
	 * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public AzuquaResponse telemetryMetrics(String execId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.telemetryMetrics.replace(":alias", this.alias).replace(":exec", execId);
        AzuquaResponse out = azuqua.makeRequest("GET", path, "");
		return out;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Azuqua getAzuqua() {
		return azuqua;
	}

	public void setAzuqua(Azuqua azuqua) {
		this.azuqua = azuqua;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}