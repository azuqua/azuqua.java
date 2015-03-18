// Azuqua azuqua = new Azuqua(key, secret);
// Azuqua.Flo flo = azuqua.new Flo();
public class Flo {
	private static boolean DEBUG = true;
	private String name;
	private String alias;
	private Azuqua azuqua;

	public Flo(String name, String alias){
		this.name = name;
		this.alias = alias;			
	}
	
	public Flo(){}
	
	public String getName(){ return name; }
	public String getAlias(){ return alias; }
	public void setName(String name){ this.name = name; }
	public void setAlias(String alias){ this.alias = alias; }
	public void setAzuqua(Azuqua azuqua) { this.azuqua = azuqua; }

	
	/**
	 * Wrapper for System.out.println.
	 * @param objects
	 */
	public static void out(Object... objects) {
		if (DEBUG) {
			for(Object object : objects) {
				System.out.println(object);
			}
		}
	}
	
	public String invoke(String json) throws AzuquaException{
		String path = Azuqua.invokeRoute.replace(":id", this.alias);
		out("path " + path);
		out("json " + json);
		String out = null;
		try {
			out = azuqua.makeRequest("POST", path, json);
//			} catch (InvalidKeyException|NoSuchAlgorithmException|IllegalStateException|IOException e) {
		} catch (Exception e) {
			throw new AzuquaException(e);
		}
		return out;
	}
	
}