package instashare.instashare;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginService {
    private static String jwt_token = "";

    //TODO: For Development, you will need to alter this string so that it points to your computer's localhost
    private static final String baseURL = "http://10.110.32.66:8000/api/token/";

    public static String login(String username, String password) throws IOException {

        //build post object
        String postJSON = "{\"username\":" + "\"" + username + "\","
                + "\"password\":" + "\"" + password + "\"}";

        //System.out.println(postJSON);

        //build request
        URL obj = new URL(baseURL);
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setDoOutput(true);
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setRequestProperty("Accept", "application/json");

        //write request to api
        DataOutputStream wr = new DataOutputStream(postConnection.getOutputStream());
        wr.write(postJSON.getBytes());
        Integer responseCode = postConnection.getResponseCode();
        wr.flush();
        wr.close();

        //parse response
        BufferedReader bufferedReader;

        if (responseCode > 199 && responseCode < 300) {
            String json_response = "";
            InputStreamReader in = new InputStreamReader(postConnection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
                json_response += text;
            }
            Log.i("LOGIN_TEST", json_response);
            try {
                JSONObject json = new JSONObject(json_response);
                jwt_token = json.getString("access");
                Log.i("LOGIN_JWT", jwt_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(postConnection.getErrorStream()));
            System.out.println(responseCode);
            System.out.println();
        }
/*
        //get jwt token from response
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();

        // Prints the response
        System.out.println(content.toString());
*/
        return jwt_token;
    }

    private static void logout() {
        jwt_token = "";
    }
}
