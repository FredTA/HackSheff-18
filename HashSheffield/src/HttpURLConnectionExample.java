import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class HttpURLConnectionExample {

	private final String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	public StringBuffer sendGet() throws Exception {

		String url = "https://haveibeenpwned.com/api/v2/breaches";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
			new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		return response;
		
	}
		
	void printStr(StringBuffer rsp) {
		
		String str = rsp.toString();
		
		String strNew1 = str.substring(1, str.length()-1);
		JSONObject obj2 = new JSONObject(strNew1);
		
		String n = obj2.getString("Domain");
		String a = obj2.getString("BreachDate");
		
		System.out.println(n + "/n " + a); 

		strNew1 = strNew1.replaceFirst("Domain","");
		
		
	}
}
