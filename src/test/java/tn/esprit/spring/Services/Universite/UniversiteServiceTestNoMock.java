package tn.esprit.spring.Services.Universite;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.List;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Utiliser H2 en mémoire
@SpringBootTest
@ActiveProfiles("test")
public class UniversiteServiceTestNoMock {

    @Autowired
    private UniversiteService universiteService;

    @Autowired
     UniversiteRepository universiteRepository;


    private Universite universite;

    @BeforeEach
    public void setUp() {

        universite = new Universite();
        universite.setNomUniversite("Test University");
        universite.setAdresse("123 Test St");
        universite = universiteRepository.save(universite);
    }


    @Test
   void testFindAll() {
        List<Universite> universites = universiteRepository.findAll();
        assertThat(universites).isNotNull();
        assertThat(universites.size()).isGreaterThan(0);
    }



    @Test
    void test_addOrUpdate_ShouldSaveUniversite() {
        Universite newUniversite = new Universite();
        newUniversite.setNomUniversite("Université de Paris");
        newUniversite.setAdresse("123 Rue de Paris");

        Universite savedUniversite = universiteService.addOrUpdate(newUniversite);

    }
    @Test
    void testFindById() {
      Long id = universite.getIdUniversite();
       Universite foundUniversite = universiteService.findById(id);

    }

    @Test
     void testDeleteById() {
        long idToDelete = universite.getIdUniversite();
        universiteService.deleteById(idToDelete);

        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.findById(idToDelete);
        });
    }

    @Test
    void testDelete() {
        // Supprimer l'entité universite
        universiteService.delete(universite);

        // Vérifier que l'entité n'existe plus dans le dépôt
        assertThrows(IllegalArgumentException.class, () -> universiteService.findById(universite.getIdUniversite()),
                "L'université devrait être supprimée et introuvable.");
    }

}
