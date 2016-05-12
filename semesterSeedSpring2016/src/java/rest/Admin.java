package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Passenger;
import entity.Reservation;
import facades.UserFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("admin")
@RolesAllowed("Admin")
public class Admin {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    UserFacade fc = new UserFacade();

//  @GET
//  @Produces(MediaType.APPLICATION_JSON)
//  public String getSomething(){
//    String now = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
//    return "{\"message\" : \"This message was delivered via a REST call accesible by only authenticated ADMINS\",\n"
//            +"\"serverTime\": \""+now +"\"}"; 
//  }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public String getUsers() {
        JsonArray result = new JsonArray();
        List<entity.User> users = fc.getUsers();
        for (entity.User u : users) {
            JsonObject p1 = new JsonObject();
            p1.addProperty("username", u.getUserName());
            JsonArray roles = new JsonArray();
            List<entity.Role> r1 = u.getRoles();
            for (entity.Role r : r1) {
                JsonObject p2 = new JsonObject();
                p2.addProperty("role", r.getRoleName());
                roles.add(p2);
            }
            p1.add("roles", roles);

            result.add(p1);
        }
        return gson.toJson(result);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reservations")
    public String getReservations() {
        JsonArray result = new JsonArray();
        List<Reservation> reservations = fc.getReservations();
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
            p1.addProperty("airlineName", r.getAirlineName());

            JsonArray passengers = new JsonArray();
            List<Passenger> p = r.getPassengers();
            for (Passenger pas : p) {
                JsonObject p2 = new JsonObject();
                p2.addProperty("firstName", pas.getFirstname());
                p2.addProperty("lastName", pas.getLastname());
                passengers.add(p2);
            }
            p1.add("passengers", passengers);

            p1.addProperty("username", r.getUser().getUserName());

            result.add(p1);
        }
        return gson.toJson(result);
    }

    @DELETE
    @Path("/user/{username}")
    @Produces("application/json")
    public String deleteUser(@PathParam("username") String username) {
        entity.User c = fc.deleteUser(username);
        //return gson.toJson(c);
        JsonObject jo = new JsonObject();
        jo.addProperty("userName", c.getUserName());
        return gson.toJson(jo);
    }

    @DELETE
    @Path("/reservation/{id}")
    @Produces("application/json")
    public String deleteReservation(@PathParam("id") int id) {
        Reservation r = fc.deleteReservation(id);
        //return gson.toJson(c);
        JsonObject jo = new JsonObject();
        jo.addProperty("id", r.getId());
        jo.addProperty("airlineName", r.getAirlineName());
        return gson.toJson(jo);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/editReservation")
    public String editReservation(String reservation) {
        Reservation r = gson.fromJson(reservation, Reservation.class);
        r = fc.editReservation(r);
        JsonObject status = new JsonObject();
        status.addProperty("status", "succes");
        return gson.toJson(status);
    }

}
