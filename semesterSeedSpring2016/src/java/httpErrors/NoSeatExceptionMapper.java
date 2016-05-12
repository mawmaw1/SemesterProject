/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpErrors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Magnus
 */
@Provider
public class NoSeatExceptionMapper implements ExceptionMapper<NoSeatException> {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    ServletContext context;

    @Override
    public Response toResponse(NoSeatException e) {
        JsonObject error = new JsonObject();
        JsonObject errorDetail = new JsonObject();
        int statusCode = 405;
        errorDetail.addProperty("code", statusCode);
        errorDetail.addProperty("message", e.getMessage());
        error.add("error", errorDetail);
        return Response.status(statusCode).entity(gson.toJson(error)).type(MediaType.APPLICATION_JSON).build();
    }

}
