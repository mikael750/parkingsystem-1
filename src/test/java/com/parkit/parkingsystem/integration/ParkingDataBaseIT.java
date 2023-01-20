package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.ParkingDate;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static ParkingDate parkingDate;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        //GIVEN

        Date date = new Date();
        when(parkingDate.getParkingDate()).thenReturn(date);//il ne faut pas que parkingDate soit null

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, parkingDate);

        //ACT

        parkingService.processIncomingVehicle();

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        Ticket ticket; //genere un ticket
        ticket = ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber());//on lui donne le numero du vehicule

        //ASSERT

        //assertEquals(expectedTicket,actualTicket);
        assertNotNull(ticket);//verifie si ticket est bien sauver dans la DB
        ParkingSpot placeParking = ticket.getParkingSpot();
        assertTrue(parkingSpotDAO.updateParking(placeParking));//verifie si le tableau du Parking est mis à jour avec disponibiliter
        boolean libre = placeParking.isAvailable();
        assertFalse(libre);// verifie si une place est libre.
    }

    @Test
    public void testParkingLotExit(){
        //GIVEN

        testParkingACar();
        String code = inputReaderUtil.readVehicleRegistrationNumber();//on lui donne le numero du vehicule
        long timeIn = ticketDAO.getTicket(code).getInTime().getTime();
        long timeOut = timeIn + (60*60*1000);//convertis en heure
        Date dateSortie = new Date(timeOut);
        when(parkingDate.getParkingDate()).thenReturn(dateSortie);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, parkingDate);

        //ACT

        parkingService.processExitingVehicle();

        //TODO: check that the fare generated and out time are populated correctly in the database

        //ASSERT

        assertTrue(ticketDAO.getTicket(code).getPrice() > 0.0);//le prix est plus grace au taux
        assertNotNull(ticketDAO.getTicket(code).getOutTime());//est bien dans la dataBase

    }
}
