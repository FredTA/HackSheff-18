import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class HttpURLConnectionExample {

	private final String USER_AGENT = "Mozilla/5.0";
	int h = 0;

	// HTTP GET request
	public String sendGet() throws Exception {

		String url = "https://haveibeenpwned.com/api/v2/breaches";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();


		BufferedReader in = new BufferedReader(
			new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		String strNew = response.toString();
		//String strNew = str.substring(1, str.length()-1);
		return strNew;
		
	}
		
	void printStr(String strNew) {
		
		
		JSONArray arr = new JSONArray(strNew);
		JSONObject obj2 = arr.getJSONObject(h);
		String n = obj2.getString("Domain");
		String a = obj2.getString("BreachDate");
		h += 1;
		
		System.out.println(n + " " + a); 
		
		
		
		if (h<arr.length()) {
			printStr(strNew);
		}
	}
}
