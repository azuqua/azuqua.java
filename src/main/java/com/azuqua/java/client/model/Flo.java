package com.azuqua.java.client.model;
import com.azuqua.java.client.Azuqua;
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

	/**
	 * The name of the FLO.
	 */
	private String name;

	/**
	 * The alias of the FLO.
	 */
	private String alias;

	/**
	 * Reference to the Azuqua object. Needed for the access key|secret and also for making requests to Azuqua.
	 */
	private Azuqua azuqua;

	public Flo(String name, String alias){
		this.name = name;
		this.alias = alias;
	}

	/**
	 * Invokes a flo.
	 * @param json A serialized json.
	 * @return
     * @throws IOException There was a problem establishing a connection to the Azuqua server.
     * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
     * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public AzuquaResponse invoke(String json) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.invokeRoute.replace(":id", this.alias);
		AzuquaResponse out;
		out = azuqua.makeRequest("POST", path, json);
		return out;
	}

	/**
	 *
	 * @param execId The exec id of the flo.
	 * @return AzuquaResponse object.
	 * @throws IOException There was a problem establishing a connection to the Azuqua server.
	 * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
	 * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public AzuquaResponse telemetryData(String execId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.telemetryData.replace(":alias", this.alias).replace(":exec", execId);
		AzuquaResponse out;
		out = azuqua.makeRequest("GET", path, "");
		return out;
	}

	/**
	 *
	 * @param execId The exec id of the flo.
	 * @return AzuquaResponse object
	 * @throws IOException There was a problem establishing a connection to the Azuqua server.
	 * @throws InvalidKeyException There was a problem generating the hash for the x-api-hash for the header.
	 * @throws NoSuchAlgorithmException The HmacSHA256 algorithm isn't available for use.
	 */
	public AzuquaResponse telemetryMetrics(String execId) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		String path = Azuqua.telemetryMetrics.replace(":alias", this.alias).replace(":exec", execId);
		AzuquaResponse out;
		out = azuqua.makeRequest("GET", path, "");
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