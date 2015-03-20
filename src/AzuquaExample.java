import java.util.List;


public class AzuquaExample {
	private static String access_key = "foo";
	private static String access_secret = "bar";
	
	public static void main(String[] argv) throws Exception {
		Azuqua azuqua = new Azuqua(access_key, access_secret);
		List<Flo> flos = (List<Flo>) azuqua.getFlos();
				
		for(Flo flo : flos) {
			System.out.println("Alias: " + flo.getAlias());
			if(flo.getAlias().equals("foo")){
				String response = flo.invoke("{\"b\":\"foo@azuqua.com\"}");
				System.out.println(response);
			}
		}
	}
}
