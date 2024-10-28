package tn.esprit.spring.Services.Reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    ReservationRepository repo;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Override
    public Reservation addOrUpdate(Reservation r) {
        if (r == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        return repo.save(r);
    }

    @Override
    public List<Reservation> findAll() {
        return repo.findAll();
    }

    @Override
    public Reservation findById(String id) {
        if (id == null || !id.matches("\\d+")) {
            throw new IllegalArgumentException("Reservation id cannot be null");
        }
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(String id) {
        if (id == null || !id.matches("\\d+")) {
            throw new IllegalArgumentException("Can't delete a null or invalid id");
        }
        repo.deleteById(id);
    }

    @Override
    public void delete(Reservation r) {
        if (r == null) {
            throw new IllegalArgumentException("Can't delete a non existant reservation");
        }
        repo.delete(r);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        // Pour l’ajout de Réservation, l’id est un String et c’est la concaténation de "numeroChambre",
        // "nomBloc" et "cin". Aussi, l’ajout ne se fait que si la capacite maximale de la chambre est encore non atteinte.

        // Début "récuperer l'année universitaire actuelle"
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        // Fin "récuperer l'année universitaire actuelle"
        Reservation res = new Reservation();
        Chambre c = chambreRepository.findByNumeroChambre(numChambre);
        if (c == null) {
            log.warn("Chambre not found for numeroChambre: {}", numChambre);
            return null; // Return early if chambre is not found
        }
        Etudiant e = etudiantRepository.findByCin(cin);
        if (e == null) {
            log.warn("Etudiant not found for cin: {}", cin);
            return null; // Return early if etudiant is not found
        }
        boolean ajout = false;
        int numRes = chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(c.getIdChambre(), dateDebutAU, dateFinAU);
        logger.error("Reservation number: {}", numRes);
        switch (c.getTypeC()) {
            case SIMPLE:
                if (numRes < 1) {
                    ajout = true;
                } else {
                    log.info("Chambre simple remplie !");
                }
                break;
            case DOUBLE:
                if (numRes < 2) {
                    ajout = true;
                } else {
                    log.info("Chambre double remplie !");
                }
                break;
            case TRIPLE:
                if (numRes < 3) {
                    ajout = true;
                } else {
                    log.info("Chambre triple remplie !");
                }
                break;
        }
        if (ajout) {
            res.setEstValide(false);
            res.setAnneeUniversitaire(LocalDate.now());
            // AU-BLOC-NumChambre-CIN --> Exemple: 2023/2024-Bloc A-1-123456789

            res.setIdReservation(dateDebutAU.getYear() + "/" + dateFinAU.getYear() + "-" + c.getBloc().getNomBloc() + "-" + c.getNumeroChambre() + "-" + e.getCin());
            res.getEtudiants().add(e);
            res.setEstValide(true);
            res = repo.save(res);
            c.getReservations().add(res);
            chambreRepository.save(c);
        }
        return res;
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        if (debutAnnee.isAfter(finAnnee)) {
            throw new IllegalArgumentException("Debut annee doit etre avant fin annee");
        }
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cinEtudiant) {
        if (cinEtudiant <= 0) {
            return "L'ID de l'étudiant est invalide.";
        }
        Reservation r = repo.findByEtudiantsCinAndEstValide(cinEtudiant, true);
        if (r == null) {
            return "Aucune réservation trouvée pour l'étudiant.";
        }
        Chambre c = chambreRepository.findByReservationsIdReservation(r.getIdReservation());
        if (c == null) {
            return "Aucune chambre associée à la réservation.";
        }
        c.getReservations().remove(r);
        chambreRepository.save(c);
        repo.delete(r);
        return "La réservation " + r.getIdReservation() + " est annulée avec succés";
    }

    @Override
    public void affectReservationAChambre(String idRes, long idChambre) {
        Reservation r = repo.findById(idRes).get();
        Chambre c = chambreRepository.findById(idChambre).get();
        // Parent: Chambre , Child: Reservation
        // On affecte le child au parent
        boolean alreadyAssigned = false;
        for (Reservation existingReservation : c.getReservations()) {
            if (existingReservation.getIdReservation().equals(r.getIdReservation())) {
                alreadyAssigned = true; // Reservation is already assigned
                break;
            }
        }
        if (!alreadyAssigned) {
            c.getReservations().add(r);
            chambreRepository.save(c);
        }
    }

        @Override
        public void annulerReservations () {
            // Début "récuperer l'année universitaire actuelle"
            LocalDate dateDebutAU;
            LocalDate dateFinAU;
            int year = LocalDate.now().getYear() % 100;
            if (LocalDate.now().getMonthValue() <= 7) {
                dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
                dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
            } else {
                dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
                dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
            }
            // Fin "récuperer l'année universitaire actuelle"
            for (Reservation reservation : repo.findByEstValideAndAnneeUniversitaireBetween(true, dateDebutAU, dateFinAU)) {
                reservation.setEstValide(false);
                repo.save(reservation);
                log.info("La reservation " + reservation.getIdReservation() + " est annulée automatiquement");
            }
        }
}
