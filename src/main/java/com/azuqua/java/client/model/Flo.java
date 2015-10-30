package com.azuqua.java.client.model;
import com.azuqua.java.client.Azuqua;
import com.azuqua.java.client.AzuquaException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * <p>
 *     Represents an Azuqua FLO.
 * </p>
 *
 * <p><b>Usage</b></p>
 * <pre>
 *     Azuqua azuqua = new Azuqua(key, secret);
 *
 * </pre>
 *
 *
 *
 * @author quyle
 *
 */
@Log
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flo {
	private String name;
	private String alias;
	private Azuqua azuqua;

	public Flo(String name, String alias){
		this.name = name;
		this.alias = alias;
	}

	public String invoke(String json) throws AzuquaException{
		String path = Azuqua.invokeRoute.replace(":id", this.alias);
		String out;
		try {
			out = azuqua.makeRequest("POST", path, json);
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}

	public String telemetryData(String execId) throws AzuquaException {
		String path = Azuqua.telemetryData.replace(":alias", this.alias).replace(":exec", execId);
		String out;
		try {
			out = azuqua.makeRequest("GET", path, "");
		}
		catch(Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}

	public String telemetryMetrics(String execId) throws AzuquaException {
		String path = Azuqua.telemetryMetrics.replace(":alias", this.alias).replace(":exec", execId);
		String out;
		try {
			out = azuqua.makeRequest("GET", path, "");
		}
		catch(Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
}