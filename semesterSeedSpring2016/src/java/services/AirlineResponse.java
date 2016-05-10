package services;
import entity.Airline;
import facades.UserFacade;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class AirlineResponse {

    //private static String url = "http://angularairline-plaul.rhcloud.com/api/flightreservation";
    private UserFacade uf = new UserFacade();
    
    public String getReservationResponse(String json, String airlineName, String flightID) throws MalformedURLException, IOException{
        
        Airline airURL = (Airline) uf.getAirlineURL(airlineName);
        String url = airURL.getUrl();
        
        HttpURLConnection con = (HttpURLConnection) new URL(url+"/reservation/"+flightID).openConnection();
        con.setRequestProperty("Content-Type", "application/json;");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Method", "POST");
        con.setDoOutput(true);
        PrintWriter pw = new PrintWriter(con.getOutputStream());
        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
        }
        int HttpResult = con.getResponseCode();
        InputStreamReader is = HttpResult < 400 ? new InputStreamReader(con.getInputStream(), "utf-8")
                : new InputStreamReader(con.getErrorStream(), "utf-8");
        Scanner responseReader = new Scanner(is);
        String response = "";
        while (responseReader.hasNext()) {
            response += responseReader.nextLine() + System.getProperty("line.separator");
        }
        return response;
    }
}
