import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;



/**
 * access key: ad99dec1ca1d28a89fbd1dc817f1654f947964b0
 * access secret: 77f0e63c746075f28e20187e7e6745a2161c4658806c2d2609ea346da4cb2162
 * @author quyvle
 *
 */
public class AzuquaExample {
	private static String access_key = "ad99dec1ca1d28a89fbd1dc817f1654f947964b0";
	private static String access_secret = "77f0e63c746075f28e20187e7e6745a2161c4658806c2d2609ea346da4cb2162";
	
	private static void out(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}
	
	public static void main(String[] argv) throws Exception {
		Azuqua azuqua = new Azuqua(access_key, access_secret);
		List<Flo> flos = (List<Flo>) azuqua.getFlos();
		
		// you can also manually refresh the flo cache
		// flos = (List<Flo>) azuqua.getFlos(true);
		
		for(Flo flo : flos) {
			System.out.println("Alias: " + flo.getAlias());
			System.out.println("Name: " + flo.getName());
			String response = flo.invoke("{\"abc\":\"this is a test.\"}");
			out(response);
		}
	}
}