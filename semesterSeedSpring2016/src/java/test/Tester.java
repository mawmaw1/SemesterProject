/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

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
        List<Reservation> r = uf.getUserReservation("user");
        for (Reservation r1 : r) {
            System.out.println(r1.getDate());
        }
    }
}
