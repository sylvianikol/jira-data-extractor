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
        String biExportURL = "getbusinessintelligenceexport/1.0/message";
        String analysisStartDate = "15-FEB-21";
        String analysisEndDate = "17-FEB-21";
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
            jsonData = getJsonData(baseURL, biExportURL, jSessionId, analysisStartDate, analysisEndDate);
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
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

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

    private static String parseJSessionId(String input) {
        String jSessionId = "";

        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(input);
            JSONObject session = (JSONObject) object.get("session");
            jSessionId = (String) session.get("value");

        } catch (Exception ex) {
            System.out.println("Error in parseJSessionId(): " + ex.getMessage());
            jSessionId = "ERROR";
        }

        System.out.println("jSessionId: ");
        System.out.println(jSessionId);
        return jSessionId;
    }

    private static String getJsonData(String baseURL, String biExportURL, String jSessionId,
                                      String analysisStartDate, String analysisEndDate) {
        String jsonData = "";

        try {
//            URL url = new URL(baseURL + biExportURL +
//                    "?startDate=" + analysisStartDate +
//                    "&endDate=" + analysisEndDate);
//            URL url = new URL(baseURL + "api/2/user" + "?username=admin");
            URL url = new URL(baseURL + "api/2/issue/picker" + "?currentJQL=assignee%3Dadmin");
            String cookie = "JSESSIONID=" + jSessionId;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cookie", cookie);

            if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output = "";

                while ((output = bufferedReader.readLine()) != null) {
                    jsonData += output;
                }

                connection.disconnect();
            }
        } catch (Exception ex) {
            System.out.println("Error in getJsonData(): " + ex.getMessage());
            jsonData = "ERROR";
        }

        System.out.println("jsonData: ");
        System.out.println(jsonData);
        return jsonData;
    }

    private static String formatAsCsv() {
        String csvData = "";
        return csvData;
    }

}
