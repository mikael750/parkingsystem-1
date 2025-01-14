package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, long n_tickets){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //il faudrait remplacer getHours() par getTime() et mettre en minutes /1000/60.
        long inMilli = ticket.getInTime().getTime();
        long outMilli = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long dureMinutes = outMilli/1000/60 - inMilli/1000/60;
        //Le prix du Ticket est different selon le taux, la durer et le vehicule
        double taux = 1.0;
        int duration = (int)dureMinutes / 60;
        if (dureMinutes < 30) {//Story 1: Free 30-min parking
            taux = 0.0;
        } else if (dureMinutes < 60) {//calculateFare(Car/Bike)WithLessThanOneHourParkingTime
            taux = 0.75; // give 3/4th parking fare
            duration = 1;
        }

        double discount;//Story 2
        if (n_tickets > 1){
            discount = 5;
        }else{
            discount = 0;
        }

        if (discount > 0) {
            System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(taux * duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(taux * duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        if (discount == 5) {
            ticket.setPrice((ticket.getPrice()/1.05));
        }
    }
}