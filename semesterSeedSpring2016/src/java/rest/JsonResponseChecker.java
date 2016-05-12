/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author kristoffernoga
 */
public class JsonResponseChecker {

    public boolean checkJson(JsonObject object) {
        if (object.get("flights") != null
                && 
                 object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("date") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("destination") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("flightID") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("flightNumber") != null             
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("numberOfSeats") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("origin") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("totalPrice") != null
                || object.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("traveltime") != null) {
            return true;
        } else {
            return false;
        }
        //String test = jsonObject.get("flights").getAsJsonArray().get(0).getAsJsonObject().get("flightID").getAsString();
    }

}
