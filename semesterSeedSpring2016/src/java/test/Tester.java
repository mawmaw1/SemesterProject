/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import entity.Airline;
import entity.Reservation;
import facades.UserFacade;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kristoffernoga
 */
public class Tester {
    public static void main(String[] args) {
        UserFacade uf = new UserFacade();
//        List<Airline> u = uf.getAllAirlineURL();
//        for (Airline u1 : u) {
//            System.out.println(u1.getUrl());
//        }
        uf.deleteReservation(1);
        
    }
}
