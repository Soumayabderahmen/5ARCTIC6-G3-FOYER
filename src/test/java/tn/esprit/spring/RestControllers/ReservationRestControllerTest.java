package tn.esprit.spring.RestControllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.Services.Reservation.IReservationService;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


class ReservationRestControllerTest {

    @Test
    void testAddOrUpdate_ValidReservation() {
        Reservation reservation = new Reservation();
        // Set reservation properties (id, chambre, etudiant, etc.)

        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.addOrUpdate(reservation)).thenReturn(reservation);

        ReservationRestController controller = new ReservationRestController(mockService);

        Reservation savedReservation = controller.addOrUpdate(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation, savedReservation);
        Mockito.verify(mockService).addOrUpdate(reservation);
    }

    @Test
    public void testAddOrUpdate_NullReservation() {
        ReservationRestController controller = new ReservationRestController(null);

        assertThrows(NullPointerException.class, () -> controller.addOrUpdate(null));
    }

    @Test
    void testFindAll_NotEmpty() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.findAll()).thenReturn(reservations);

        ReservationRestController controller = new ReservationRestController(mockService);

        List<Reservation> retrievedReservations = controller.findAll();

        assertNotNull(retrievedReservations);
        assertFalse(retrievedReservations.isEmpty());
        Mockito.verify(mockService).findAll();
    }

    @Test
    public void testFindAll_Empty() {
        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.findAll()).thenReturn(Collections.emptyList());

        ReservationRestController controller = new ReservationRestController(mockService);

        List<Reservation> retrievedReservations = controller.findAll();

        assertNotNull(retrievedReservations);
        assertTrue(retrievedReservations.isEmpty());
        Mockito.verify(mockService).findAll();
    }

    @Test
    void testFindById_ExistingId() {
        String id = "123";
        Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.findById(id)).thenReturn(reservation);

        ReservationRestController controller = new ReservationRestController(mockService);

        Reservation retrievedReservation = controller.findById(id);

        assertNotNull(retrievedReservation);
        assertEquals(reservation, retrievedReservation);
        Mockito.verify(mockService).findById(id);
    }

    @Test
    public void testFindById_NonexistentId() {
        String id = "456";
        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.findById(id)).thenReturn(null);

        ReservationRestController controller = new ReservationRestController(mockService);

        Reservation retrievedReservation = controller.findById(id);

        assertNull(retrievedReservation);
        Mockito.verify(mockService).findById(id);
    }

    @Test
    void testDelete_ExistingReservation() {
        Reservation reservation = new Reservation();
        ReservationService mockService = Mockito.mock(ReservationService.class);

        ReservationRestController controller = new ReservationRestController(mockService);
        controller.delete(reservation);

        Mockito.verify(mockService).delete(reservation);
    }

    @Test
    void testDeleteById_ExistingID() {
        String id = "123";
        IReservationService mockService = Mockito.mock(IReservationService.class);

        ReservationRestController controller = new ReservationRestController(mockService);

        controller.deleteById(id);

        Mockito.verify(mockService).deleteById(id);
    }

    @Test
    void testDeleteById_NonexistentId() {
        String id = "456";
        IReservationService mockService = Mockito.mock(IReservationService.class);

        ReservationRestController controller = new ReservationRestController(mockService);

        controller.deleteById(id);

        Mockito.verify(mockService).deleteById(id);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant_Success() {
        Long numChambre = 1L;
        long cin = 123456789L;
        Reservation expectedReservation = new Reservation(); // Create a sample reservation

        IReservationService mockService = Mockito.mock(IReservationService.class);
        Mockito.when(mockService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin)).thenReturn(expectedReservation);

        ReservationRestController controller = new ReservationRestController(mockService);

        // Act
        Reservation result = controller.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        // Assert
        assertEquals(expectedReservation, result);
        Mockito.verify(mockService).ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant_InvalidNumChambre() {
        Long numChambre = null;
        long cin = -123; // Invalid CIN value

        ReservationService mockService = Mockito.mock(ReservationService.class);

        // Create the controller with the mocked service
        ReservationRestController controller = new ReservationRestController(mockService);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin));

        // Verify the service method was never called
        Mockito.verify(mockService, Mockito.never()).ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant_InvalidCin() {
        Long numChambre = 1L;
        long cin = -123; // Invalid CIN value

        ReservationService mockService = Mockito.mock(ReservationService.class);

        // Create the controller with the mocked service
        ReservationRestController controller = new ReservationRestController(mockService);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin));

        // Verify the service method was never called
        Mockito.verify(mockService, Mockito.never()).ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @Test
    void testGetReservationParAnneeUniversitaire_ValidDates() {
        LocalDate debutAnnee = LocalDate.of(2024, 9, 15);
        LocalDate finAnnee = LocalDate.of(2025, 6, 30);
        long expectedCount = 10;
        ReservationService mockService = Mockito.mock(ReservationService.class);
        Mockito.when(mockService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee)).thenReturn(expectedCount);

        ReservationRestController controller = new ReservationRestController(mockService);

        long retrievedCount = controller.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        assertEquals(expectedCount, retrievedCount);
        Mockito.verify(mockService).getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @Test
    void testGetReservationParAnneeUniversitaire_InvalidDates() {
        LocalDate debutAnnee = LocalDate.of(2025, 10, 1);
        LocalDate finAnnee = LocalDate.of(2024, 9, 15);
        ReservationService mockService = Mockito.mock(ReservationService.class);

        ReservationRestController controller = new ReservationRestController(mockService);

        assertThrows(IllegalArgumentException.class, () -> controller.getReservationParAnneeUniversitaire(debutAnnee, finAnnee));
        Mockito.verify(mockService, Mockito.never()).getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @Test
    void testAnnulerReservation_Success() {
        long cinEtudiant = 123456789;
        String expectedResult = "Reservation annulée avec succès";

        IReservationService mockService = Mockito.mock(IReservationService.class);
        Mockito.when(mockService.annulerReservation(cinEtudiant)).thenReturn(expectedResult);

        ReservationRestController controller = new ReservationRestController(mockService);

        // Act
        String result = controller.annulerReservation(cinEtudiant);

        // Assert
        assertEquals(expectedResult, result);
        Mockito.verify(mockService).annulerReservation(cinEtudiant);
    }
    @Test
    void testAnnulerReservation_NotFound() {
        // Arrange
        long cinEtudiant = 123456789;
        String expectedResult = "Etudiant non trouvé";

        IReservationService mockService = Mockito.mock(IReservationService.class);
        Mockito.when(mockService.annulerReservation(cinEtudiant)).thenReturn(expectedResult);

        ReservationRestController controller = new ReservationRestController(mockService);

        // Act
        String result = controller.annulerReservation(cinEtudiant);

        // Assert
        assertEquals(expectedResult, result);
        Mockito.verify(mockService).annulerReservation(cinEtudiant);
    }
}