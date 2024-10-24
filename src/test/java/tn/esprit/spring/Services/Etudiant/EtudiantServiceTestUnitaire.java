package tn.esprit.spring.Services.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Utiliser H2 en mémoire
@ActiveProfiles("test") // Assurez-vous que votre application.properties a une configuration 'test' si nécessaire

 class EtudiantServiceTestUnitaire {
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll(); // S'assurer que la base est vide avant chaque test
    }

    @Test
    void test_addOrUpdate_ShouldSaveEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(savedEtudiant.getIdEtudiant()); // Assurez-vous que l'ID est généré
        assertEquals("John", savedEtudiant.getNomEt());
    }

    @Test
    void test_findAll_ShouldReturnAllEtudiants() {
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNomEt("John");
        etudiantRepository.save(etudiant1);

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNomEt("Jane");
        etudiantRepository.save(etudiant2);

        List<Etudiant> etudiants = etudiantService.findAll();

        assertEquals(2, etudiants.size());
        assertEquals("John", etudiants.get(0).getNomEt());
        assertEquals("Jane", etudiants.get(1).getNomEt());
    }

    @Test
    void test_findById_ShouldReturnEtudiant_WhenExists() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiantRepository.save(etudiant);

        long id = etudiant.getIdEtudiant();
        Etudiant foundEtudiant = etudiantService.findById(id);

        assertNotNull(foundEtudiant);
        assertEquals("John", foundEtudiant.getNomEt());
    }

    @Test
    void test_deleteById_ShouldDeleteEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant = etudiantRepository.save(etudiant);

        long id = etudiant.getIdEtudiant();
        etudiantService.deleteById(id);

        assertFalse(etudiantRepository.findById(id).isPresent()); // Vérifiez que l'étudiant n'est plus là
    }

    @Test
    void test_delete_ShouldDeleteEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant = etudiantRepository.save(etudiant);

        etudiantService.delete(etudiant);

        assertFalse(etudiantRepository.findById(etudiant.getIdEtudiant()).isPresent()); // Vérifiez que l'étudiant n'est plus là
    }
}
