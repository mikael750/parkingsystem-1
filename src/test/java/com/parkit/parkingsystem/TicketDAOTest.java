package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

    private static TicketDAO ticketDAO;
    private static Ticket ticket;

    @BeforeAll
    private static void setUp() {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();

        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setVehicleRegNumber("ABCDEF");
    }

    @Test
    public void getANewTicket() {
        saveANewTicket();
        assertNotNull(ticketDAO.getTicket(ticket.getVehicleRegNumber()));
    }

    @Test
    public void saveANewTicket() {
        assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void updateExistingTicket() {
        saveANewTicket();
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    public void countTicket(){
        saveANewTicket();
        assertTrue(ticketDAO.countTicket(ticket.getVehicleRegNumber())>0);
    }
}