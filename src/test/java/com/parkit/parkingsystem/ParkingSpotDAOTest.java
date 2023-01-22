package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingSpotDAOTest {
    
    @BeforeAll
    private static void setUp() {
        ParkingSpot parkingSpot1 = new ParkingSpot(1, ParkingType.CAR,true);
        ParkingSpot parkingSpot2 = new ParkingSpot(2, ParkingType.CAR,true);
        ParkingSpot parkingSpot3 = new ParkingSpot(3, ParkingType.CAR,true);
        ParkingSpot parkingSpot4 = new ParkingSpot(4, ParkingType.BIKE,true);
        ParkingSpot parkingSpot5 = new ParkingSpot(5, ParkingType.BIKE,true);


    }

    @BeforeEach
    private void setUpPerTest() {
    }

    @Test
    public void takingNextAvailableSlotForCar() {
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        //CAR slot = 1, 2, 3
        assertEquals(1,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void takingNextAvailableSlotForBike() {
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        //BIKE slot = 4, 5
        assertEquals(4,parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
    }

    @Test
    public void updateParkingAvailability() {
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR,false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

}
