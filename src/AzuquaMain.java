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
public class AzuquaMain {
	private static String access_key = "ad99dec1ca1d28a89fbd1dc817f1654f947964b0";
	private static String access_secret = "77f0e63c746075f28e20187e7e6745a2161c4658806c2d2609ea346da4cb2162";
	
	private static void out(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}
	
	public static void main(String[] argv) throws Exception {
		Azuqua azuqua = new Azuqua(access_key, access_secret);
		
		TimeZone tz = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    df.setTimeZone(tz);
	    String timestamp = df.format(new Date());
	    
	    timestamp="2015-03-16T05:46:47.115Z";
		
		azuqua.signDataQ("", "get", "/account/flos", timestamp);
		List<Azuqua.Flo> flos = (List<Azuqua.Flo>) azuqua.getFlos(false);
//		out(flos);
//		for (Azuqua.Flo flo : flos) {
//			out()
//		}
	}
}
