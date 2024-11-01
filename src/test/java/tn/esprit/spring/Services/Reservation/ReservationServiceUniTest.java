package tn.esprit.spring.Services.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Utiliser H2 en mémoire
@ActiveProfiles("test")

class ReservationServiceUnitTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        reservationService = new ReservationService(reservationRepository, etudiantRepository);
        // Clean repository before each test (optional)
        reservationRepository.deleteAll();
    }

    @Test
    void addOrUpdate_shouldSaveReservation() {
        // Given
        Reservation reservation = new Reservation();
        // Set properties for reservation as needed, e.g.:
        reservation.setIdReservation("2023/2024-Bloc A-1-123456789");
        reservation.setEstValide(true);
        // ... set other properties …

        // When
        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        // Then
        assertNotNull(savedReservation);
        assertEquals(reservation.getIdReservation(), savedReservation.getIdReservation());
    }

    @Test
    void findAll_shouldReturnAllReservations() {
        // Given
        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("2023/2024-Bloc A-1-123456789");
        reservation1.setEstValide(true);
        reservationService.addOrUpdate(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setIdReservation("2023/2024-Bloc A-2-987654321");
        reservation2.setEstValide(false);
        reservationService.addOrUpdate(reservation2);

        // When
        List<Reservation> reservations = reservationService.findAll();

        // Then
        assertEquals(2, reservations.size());
    }

    @Test
    void findById_shouldReturnReservationById() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-1-123456789");
        reservation.setEstValide(true);
        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        // When
        Reservation foundReservation = reservationService.findById(savedReservation.getIdReservation());

        // Then
        assertNotNull(foundReservation);
        assertEquals(savedReservation.getIdReservation(), foundReservation.getIdReservation());
    }

    @Test
    void deleteById_shouldRemoveReservation() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-1-123456789");
        reservation.setEstValide(true);
        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        // When
        reservationService.deleteById(savedReservation.getIdReservation());

        // Then
        assertThrows(Exception.class, () -> reservationService.findById(savedReservation.getIdReservation()));
    }

    @Test
    void delete_shouldRemoveReservation() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-1-123456789");
        reservation.setEstValide(true);
        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        // When
        reservationService.delete(savedReservation);

        // Then
        assertThrows(Exception.class, () -> reservationService.findById(savedReservation.getIdReservation()));
    }
}
