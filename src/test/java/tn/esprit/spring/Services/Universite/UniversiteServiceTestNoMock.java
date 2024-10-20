package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UniversiteServiceTestNoMock {

    @Autowired
    UniversiteService universiteService;

    @Autowired
    UniversiteRepository universiteRepository;

    @Test
    public void testAddOrUpdate() {
        Foyer foyer = new Foyer(); // Remplir les champs requis de Foyer
        Universite universite = Universite.builder()
                .nomUniversite("Université Test")
                .adresse("Adresse Test")
                .foyer(foyer)
                .build();
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotNull(savedUniversite);
        assertEquals("Université Test", savedUniversite.getNomUniversite());
        assertEquals("Adresse Test", savedUniversite.getAdresse());
    }

    @Test
    public void testFindAll() {
        List<Universite> universites = universiteService.findAll();
        assertNotNull(universites);
        assertFalse(universites.isEmpty());
    }

    @Test
    public void testFindById() {
        Foyer foyer = new Foyer();
        Universite universite = Universite.builder()
                .nomUniversite("Université Test FindById")
                .adresse("Adresse Test")
                .foyer(foyer)
                .build();
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());
        assertNotNull(foundUniversite);
        assertEquals(savedUniversite.getIdUniversite(), foundUniversite.getIdUniversite());
    }

    @Test
    public void testDeleteById() {
        Foyer foyer = new Foyer();
        Universite universite = Universite.builder()
                .nomUniversite("Université To Delete")
                .adresse("Adresse To Delete")
                .foyer(foyer)
                .build();
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        universiteService.deleteById(savedUniversite.getIdUniversite());
        assertFalse(universiteRepository.findById(savedUniversite.getIdUniversite()).isPresent());
    }
}
