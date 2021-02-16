package jiratask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Main {
    public static void main(String[] args) {

        String loginResponse = "";
        String jSessionId = "";
        String jsonData = "";
        String csvData = "";
        String writeToFileOutput = "";
        String baseURL = "http://localhost:2990/jira/rest/";
        String loginURL = "auth/1/session";
        String username = "admin";
        String password = "admin";
        boolean errorsOccured = false;

        if (!errorsOccured) {
            loginResponse = loginToJira(baseURL, loginURL, username, password);
            if (loginResponse.equals("ERROR")) { errorsOccured = true; }
        }

        if (!errorsOccured) {
            jSessionId = parseJSessionId(loginResponse);
            if (jSessionId.equals("ERROR")) { errorsOccured = true; }
        }

        if (!errorsOccured) {
            jsonData = getJsonData();
            if (jsonData.equals("ERROR")) { errorsOccured = true; }
        }

        if (!errorsOccured) {
            writeToFileOutput = formatAsCsv();
            if (writeToFileOutput .equals("ERROR")) { errorsOccured = true; }
        }

        if (!errorsOccured) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILURE");
        }
    }

    public static String loginToJira(String baseURL, String loginURL, String username, String password) {
        String loginResponse = "";
        URL url = null;
        HttpURLConnection connection = null;
        String input = "";
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        String output = null;
        try {
            url = new URL(baseURL + loginURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            input = "{\"username\":\"" + username +"\", \"password\":\"" + password + "\"}";

            outputStream = connection.getOutputStream();
            outputStream.write(input.getBytes());
            outputStream.flush();

            if (connection.getResponseCode() == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                while ((output = bufferedReader.readLine()) != null) {
                    loginResponse += output;
                }

                connection.disconnect();
            }
        } catch (Exception ex) {
            System.out.println("Error in loginToJira(): " + ex.getMessage());
            loginResponse = "ERROR";
        }

        System.out.println("Login Response: ");
        System.out.println(loginResponse);

        return loginResponse;
    }

    private static String parseJSessionId(String loginResponse) {
        String jSessionId = "";

        try {

        } catch (Exception ex) {
            System.out.println("Error in parseJSessionId(): " + ex.getMessage());
            jSessionId = "ERROR";
        }

        System.out.println("jSessionId Response: ");
        System.out.println(jSessionId);
        return jSessionId;
    }

    private static String getJsonData() {
        String jsonData = "";
        return jsonData;
    }

    private static String formatAsCsv() {
        String csvData = "";
        return csvData;
    }

}
