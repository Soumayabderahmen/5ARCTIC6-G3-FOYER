package tn.esprit.spring.Service.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository repo;
    @Mock
    private ChambreRepository chambreRepository;
    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Reservation reservation = new Reservation();
        when(repo.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.addOrUpdate(reservation);

        assertNotNull(result);
        verify(repo, times(1)).save(reservation);
    }

    @Test
    void testAddorUpdate_NullReservation() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.addOrUpdate(null);
        });
    }

    @Test
    void testAddOrUpdate_Exception() {
        Reservation reservation = new Reservation();
        when(repo.save(reservation)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            reservationService.addOrUpdate(reservation);
        });
    }

    @Test
    void testFindAll_Success() {
        List<Reservation> reservations = new ArrayList<>();
        when(repo.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(reservations, result);
        verify(repo,times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        List<Reservation> reservations = new ArrayList<>();
        when(repo.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertNotNull(result); // Ensure the result is not null
        assertTrue(result.isEmpty()); // Ensure the result is an empty list
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAll_NonEmptyList() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(repo.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(reservations.size(), result.size()); // Ensure sizes match
        assertEquals(reservations, result); // Ensure the actual lists match
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAll_NullReturn() {
        when(repo.findAll()).thenReturn(null);

        List<Reservation> result = reservationService.findAll();

        assertNull(result); // Ensure the result is null
        verify(repo, times(1)).findAll();
    }


    @Test
    void testFindById_Success() {
        String id = "1";
        Reservation reservation = new Reservation();
        when(repo.findById(id)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById(id);

        assertEquals(reservation,result);
        verify(repo, times(1)).findById(id);
    }

    @Test
    void testFindById_ReservationNotFound() {
        String id = "1";
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.findById(id);
        });
        verify(repo, times(1)).findById(id);
    }

    @Test
    void testFindById_NullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.findById(null);
        });
    }

    @Test
    void testFindById_InvalidIdFormat() {
        String invalidId = "invalid_id";
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.findById(invalidId);
        });
    }


    @Test
    void testDeleteById_Success() {
        String id = "1";

        reservationService.deleteById(id);

        verify(repo,times(1)).deleteById(id);
    }

    @Test
    void testDeleteById_NullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.deleteById(null);
        });
    }

    @Test
    void testDeleteById_InvalidIdFormat() {
        String invalidId = "invalid_id";
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.deleteById(invalidId);
        });
    }


    @Test
    void testDelete_Success() {
        Reservation reservation = new Reservation();

        reservationService.delete(reservation);

        verify(repo, times(1)).delete(reservation);
    }

    @Test
    void testDelete_NullReservation() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.delete(null);
        });
    }

    @Test
    void testDelete_NonExistingReservation() {
        Reservation reservation = new Reservation();
        doThrow(new NoSuchElementException("Reservation not found")).when(repo).delete(reservation);

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.delete(reservation);
        });

        verify(repo, times(1)).delete(reservation);
    }

    @Test
    void testDelete_AlreadyDeletedReservation() {
        Reservation reservation = new Reservation();
        doNothing().when(repo).delete(reservation);

        reservationService.delete(reservation);

        verify(repo, times(1)).delete(reservation);
    }

    @Test
    public void testAjouterReservationSimpleRoomFull() {
        Long numChambre = 1L;
        long cin = 123456789L;

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(numChambre);
        chambre.setTypeC(TypeChambre.SIMPLE);
        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cin);
        when(etudiantRepository.findByCin(cin)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(1);

        reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        verify(chambreRepository).findByNumeroChambre(numChambre);
        verify(etudiantRepository).findByCin(cin);
        verify(repo, never()).save(any(Reservation.class));

    }

    @Test
    void testAjouterReservationDoubleRoomFull() {
        Long numChambre = 1L;
        long cin = 123456789L;

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(numChambre);
        chambre.setTypeC(TypeChambre.DOUBLE);
        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cin);
        when(etudiantRepository.findByCin(cin)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(2);

        reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        verify(chambreRepository).findByNumeroChambre(numChambre);
        verify(etudiantRepository).findByCin(cin);
        verify(repo, never()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservationTripleRoomFull() {
        Long numChambre = 1L;
        long cin = 123456789L;

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(numChambre);
        chambre.setTypeC(TypeChambre.TRIPLE);
        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cin);
        when(etudiantRepository.findByCin(cin)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(3);

        reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
        verify(chambreRepository).findByNumeroChambre(numChambre);
        verify(etudiantRepository).findByCin(cin);
        verify(repo, never()).save(any(Reservation.class));
    }

    @Test
    void testAjouterReservationEtudiantNotFound() {
        // Setup
        Long numChambre = 3L;
        long cin = 555555555L;

        Chambre chambre = new Chambre();
        chambre.setIdChambre(3);
        chambre.setNumeroChambre(numChambre);
        chambre.setTypeC(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc C");
        chambre.setBloc(bloc);

        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);
        when(etudiantRepository.findByCin(cin)).thenReturn(null); // Etudiant not found

        // Execute
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        // Verify
        assertNull(result); // No reservation should be made
        verify(repo, never()).save(any(Reservation.class));
        verify(chambreRepository, never()).save(any(Chambre.class));
    }

    @Test
    void testAjouterReservationChambreNotFound() {
        // Setup
        Long numChambre = 4L;
        long cin = 666666666L;

        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(null); // Chambre not found

        // Execute
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        // Verify
        assertNull(result); // No reservation should be made
        verify(repo, never()).save(any(Reservation.class));
        verify(chambreRepository, never()).save(any(Chambre.class));
    }

    @Test
    void testGetReservationParAnneeUniversitaire_NoReservations() {
        LocalDate debutAnnee = LocalDate.of(2023, 9, 1);
        LocalDate finAnnee = LocalDate.of(2024, 6, 30);

        when(repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee)).thenReturn(0); // Return Integer

        // Execute
        long result = reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        // Verify
        assertEquals(0, result);
        verify(repo, times(1)).countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Test
    void testGetReservationParAnneeUniversitaire_SomeReservations() {
        // Setup
        LocalDate debutAnnee = LocalDate.of(2023, 9, 1);
        LocalDate finAnnee = LocalDate.of(2024, 6, 30);

        when(repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee)).thenReturn(5); // Return Integer

        // Execute
        long result = reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        // Verify
        assertEquals(5, result);
        verify(repo, times(1)).countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Test
    void testGetReservationParAnneeUniversitaire_ReservationsAcrossYears() {
        // Setup
        LocalDate debutAnnee = LocalDate.of(2023, 1, 1);
        LocalDate finAnnee = LocalDate.of(2024, 12, 31);

        when(repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee)).thenReturn(10); // Return Integer

        // Execute
        long result = reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        // Verify
        assertEquals(10, result);
        verify(repo, times(1)).countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }
    @Test
    void testAnnulerReservation_Success() {
        long cinEtudiant = 123456L;
        String reservationId = "1"; // Assuming the reservation ID is a String

        // Using the default constructor
        Reservation reservation = new Reservation();
        reservation.setIdReservation(reservationId);
        reservation.setAnneeUniversitaire(LocalDate.now()); // Set the year
        reservation.setEstValide(true); // Set valid status

        // Creating a Chambre
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101); // Example number
        reservation.setEstValide(true); // Link chambre to reservation

        // Mocking repository methods
        when(repo.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation(reservationId)).thenReturn(chambre);

        // Service call
        String result = reservationService.annulerReservation(cinEtudiant) ;

        // Assertions
        assertEquals("La réservation 1 est annulée avec succés", result);
        verify(chambreRepository).save(chambre); // Verify chambre is saved
        verify(repo).delete(reservation); // Verify reservation is deleted
    }

    @Test
    public void testAnnulerReservation_InvalidCinEtudiant() {
        long cinEtudiant = -1;

        String result = reservationService.annulerReservation(cinEtudiant);

        assertEquals("L'ID de l'étudiant est invalide.", result); // Update this line to match your expected behavior.
        verify(repo, never()).findByEtudiantsCinAndEstValide(anyLong(), anyBoolean());
    }

    @Test
    public void testAnnulerReservation_ReservationNotValid() {
        long cinEtudiant = 123456;
        Reservation reservation = new Reservation();
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(210); // Example number
        reservation.setEstValide(false);

        when(repo.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);

        String result = reservationService.annulerReservation(cinEtudiant);

        assertEquals("Aucune chambre associée à la réservation.", result); // Update this line to match your expected behavior.
        verify(chambreRepository, never()).save(any());
        verify(repo, never()).delete(any());
    }

    @Test
    public void testAnnulerReservation_NoReservationFound() {
        long cinEtudiant = 123456;

        when(repo.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(null);

        String result = reservationService.annulerReservation(cinEtudiant);

        assertEquals("Aucune réservation trouvée pour l'étudiant.", result); // Update this line to match your expected behavior.
        verify(chambreRepository, never()).save(any());
        verify(repo, never()).delete(any());
    }

    @Test
    public void testAnnulerReservation_ExceptionThrown() {
        long cinEtudiant = 123456;

        when(repo.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.annulerReservation(cinEtudiant);
        });

        assertEquals("Database error", exception.getMessage());
        verify(chambreRepository, never()).save(any());
        verify(repo, never()).delete(any());
    }

    @Test
    void testAffectReservationAChambre_Success() {
        String idRes = "res123";
        long idChambre = 1L;

        Reservation reservation = new Reservation();
        reservation.setIdReservation(idRes);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(idChambre);
        chambre.setReservations(new ArrayList<>());

        when(repo.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre(idRes, idChambre);

        assertTrue(chambre.getReservations().contains(reservation));
        verify(chambreRepository).save(chambre);
    }

    @Test
    public void testAffectReservationAChambre_ReservationNotFound() {
        String idRes = "invalidRes";
        long idChambre = 1L;

        when(repo.findById(idRes)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            reservationService.affectReservationAChambre(idRes, idChambre);
        });

        assertEquals("No value present", exception.getMessage());
        verify(chambreRepository, never()).save(any());
    }

    @Test
    public void testAffectReservationAChambre_ChambreNotFound() {
        String idRes = "res123";
        long idChambre = 999L;

        Reservation reservation = new Reservation();
        reservation.setIdReservation(idRes);

        when(repo.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            reservationService.affectReservationAChambre(idRes, idChambre);
        });

        assertEquals("No value present", exception.getMessage());
        verify(chambreRepository, never()).save(any());
    }

    @Test
    public void testAffectReservationAChambre_AlreadyAssigned() {
        String idRes = "res123";
        long idChambre = 1L;

        // Create a reservation and set its ID
        Reservation reservation = new Reservation();
        reservation.setIdReservation(idRes); // Set ID correctly

        // Create a chambre and add the reservation to its list
        Chambre chambre = new Chambre();
        chambre.setIdChambre(idChambre);
        chambre.setReservations(new ArrayList<>(Collections.singletonList(reservation))); // Already assigned

        // Mock the repository responses
        when(repo.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        // Call the method
        reservationService.affectReservationAChambre(idRes, idChambre);

        // Assertions
        assertEquals(1, chambre.getReservations().size()); // Should still be 1
        verify(chambreRepository, never()).save(any()); // Should not call save since no change is made
    }


    @Test
    public void testAffectReservationAChambre_NewAssignment() {
        String idRes = "res123";
        long idChambre = 1L;

        // Create a reservation and set its ID
        Reservation reservation = new Reservation();
        reservation.setIdReservation(idRes); // Set ID correctly

        // Create a chambre and initialize an empty reservations list
        Chambre chambre = new Chambre();
        chambre.setIdChambre(idChambre);
        chambre.setReservations(new ArrayList<>()); // Start with an empty list

        // Mock the repository responses
        when(repo.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        // Call the method
        reservationService.affectReservationAChambre(idRes, idChambre);

        // Assertions
        assertEquals(1, chambre.getReservations().size()); // Should now contain 1 reservation
        verify(chambreRepository).save(chambre); // Save should be called
    }

    @Test
    public void testAffectReservationAChambre_ExceptionThrown() {
        String idRes = "res123";
        long idChambre = 1L;

        when(repo.findById(idRes)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.affectReservationAChambre(idRes, idChambre);
        });

        assertEquals("Database error", exception.getMessage());
        verify(chambreRepository, never()).save(any());
    }


    @Test
    void testAnnulerReservations_Success() {
        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("res1");
        reservation1.setEstValide(true);

        Reservation reservation2 = new Reservation();
        reservation2.setIdReservation("res2");
        reservation2.setEstValide(true);

        List<Reservation> validReservations = Arrays.asList(reservation1, reservation2);

        // Use matchers for all parameters
        when(repo.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(validReservations);

        // Act
        reservationService.annulerReservations();

        // Assert
        assertFalse(reservation1.isEstValide());
        assertFalse(reservation2.isEstValide());
        verify(repo, times(2)).save(any(Reservation.class));
    }
    @Test
    public void testAnnulerReservations_NoReservations() {
        // Arrange
        when(repo.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        // Act
        reservationService.annulerReservations();

        // Assert
        verify(repo, never()).save(any(Reservation.class));
    }

    @Test
    public void testAnnulerReservations_SomeAlreadyInvalid() {
        // Arrange
        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("res1");
        reservation1.setEstValide(false); // Already invalid

        Reservation reservation2 = new Reservation();
        reservation2.setIdReservation("res2");
        reservation2.setEstValide(true);

        List<Reservation> validReservations = Arrays.asList(reservation1, reservation2);

        when(repo.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(validReservations);

        // Act
        reservationService.annulerReservations();

        // Assert
        assertFalse(reservation1.isEstValide());
        assertFalse(reservation2.isEstValide());
        verify(repo, times(2)).save(any(Reservation.class));
    }

    @Test
    public void testAnnulerReservations_ExceptionOnSave() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setIdReservation("res1");
        reservation.setEstValide(true);

        List<Reservation> validReservations = Arrays.asList(reservation);

        when(repo.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(validReservations);

        doThrow(new RuntimeException("Database error")).when(repo).save(any(Reservation.class));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.annulerReservations();
        });

        assertEquals("Database error", exception.getMessage());
    }



}