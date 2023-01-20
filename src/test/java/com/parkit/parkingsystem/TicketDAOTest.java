package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static TicketDAO ticketDAO;
    private static Ticket ticket;


    @BeforeAll
    private static void setUp() {
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
    }

    @BeforeEach
    private void setUpPerTest() {
        ticketDAO = new TicketDAO();
    }

    @Test
    public void getANewTicket() {
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNotNull(ticket.getVehicleRegNumber());
    }

    @Test
    public void saveANewTicket() {
        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void updateExistingTicket() {
        getANewTicket();
        saveANewTicket();
        assertTrue(ticketDAO.updateTicket(ticket));
    }
}