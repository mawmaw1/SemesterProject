/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Passenger;
import entity.Reservation;
import facades.UserFacade;
import httpErrors.NoSeatException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import security.PasswordStorage;
import services.AirlineConnector;
import services.AirlineResponse;

/**
 * REST Web Service
 *
 * @author Magnus
 */
@Path("data")
public class Data {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    UserFacade fc = new UserFacade();
    AirlineConnector ac = new AirlineConnector();
    JsonResponseChecker jrc = new JsonResponseChecker();
    AirlineResponse ar = new AirlineResponse();
  
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Data
     */
    public Data() {
    }

    /**
     * Retrieves representation of an instance of rest.Data
     *
     * @param airport
     * @param date
     * @param persons
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{airport}/{date}/{persons}")
    public String getAllFlightsFromDate(
            @PathParam("airport") String airport,
            @PathParam("date") String date,
            @PathParam("persons") int persons) throws InterruptedException, ExecutionException, IOException, NoSeatException {
        JsonArray result = new JsonArray();

        try {
            List<Future<String>> list = ac.ConnectToAirlinesFromDatePersons(airport, date, persons);
            for (Future<String> list1 : list) {

                JsonObject jsonObject = (new JsonParser()).parse(list1.get()).getAsJsonObject();
                
                //result.add(jsonObject);
            if (jrc.checkJson(jsonObject) == true) {
                result.add(jsonObject);
            }

            }
        } catch (Exception e) {
            throw new NoSeatException("Sold Out");
        }

        return gson.toJson(result);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{from}/{to}/{date}/{persons}")
    public String getAllFlightsFromToDate(
            @PathParam("from") String from,
            @PathParam("to") String to,
            @PathParam("date") String date,
            @PathParam("persons") int persons) throws InterruptedException, ExecutionException, IOException {

        JsonArray result = new JsonArray();
        List<Future<String>> list = ac.ConnectToAirlinesFromToDatePersons(from, to, date, persons);

        for (Future<String> list1 : list) {

            JsonObject jsonObject = (new JsonParser()).parse(list1.get()).getAsJsonObject();

            //result.add(jsonObject);
            if (jrc.checkJson(jsonObject) == true) {
                result.add(jsonObject);
            }

        }

        return gson.toJson(result);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reservation/{username}")
    public String getAllUserReservations(@PathParam("username") String username) throws InterruptedException, ExecutionException, IOException {

        JsonArray result = new JsonArray();
        List<Reservation> reservations = fc.getUserReservation(username);
        for (Reservation r : reservations) {
            JsonObject p1 = new JsonObject();
            p1.addProperty("id", r.getId());
            p1.addProperty("flightNumber", r.getFlightNumber());
            p1.addProperty("origin", r.getOrigin());
            p1.addProperty("destination", r.getDestination());
            p1.addProperty("date", r.getDate());
            p1.addProperty("flightTime", r.getFlightTime());
            p1.addProperty("numberOfSeats", r.getNumberOfSeats());
            p1.addProperty("reserveeName", r.getReserveeName());
            p1.addProperty("totalPrice", r.getTotalPrice());

            JsonArray passengers = new JsonArray();
            List<Passenger> p = r.getPassengers();
            for (Passenger pas : p) {
                JsonObject p2 = new JsonObject();
                p2.addProperty("firstName", pas.getFirstname());
                p2.addProperty("lastName", pas.getLastname());
                passengers.add(p2);
            }
            p1.add("passengers", passengers);

            p1.addProperty("username", username);

            result.add(p1);
        }
        return gson.toJson(result);
    }

    /**
     * PUT method for updating or creating an instance of Data
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public void addUser(String user) throws PasswordStorage.CannotPerformOperationException {
        JsonObject newUser = new JsonParser().parse(user).getAsJsonObject();
        entity.User u = new entity.User();
        u.setUserName(newUser.get("username").getAsString());
        u.setPassword(PasswordStorage.createHash(newUser.get("password").getAsString()));

        fc.addPerson(u);
        //   return gson.toJson(u);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reservation")
    public String saveReservation(String reservation) {       
        Reservation r = gson.fromJson(reservation, Reservation.class);
        r = fc.saveReservation(r);
        //return gson.toJson(r); <- causes StackOverflow error for some reason. Need to build JsonObject manually if we want the object returned.
        JsonObject status = new JsonObject();
        status.addProperty("status", "succes");
        return gson.toJson(status);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reservation/airline")
    public String getAirlineReservation(String reservation) throws IOException {
        return ar.getReservationResponse(reservation);
    }


}
