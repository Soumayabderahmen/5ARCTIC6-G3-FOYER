package tn.esprit.spring.Services.Etudiant;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest
class EtudiantServiceTest {

    @InjectMocks
    private EtudiantService etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrUpdate_ShouldSaveEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");

        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(savedEtudiant);
        assertEquals("John", savedEtudiant.getNomEt());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void findAll_ShouldReturnAllEtudiants() {
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNomEt("John");
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNomEt("Jane");

        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant1, etudiant2));

        List<Etudiant> etudiants = etudiantService.findAll();

        assertEquals(2, etudiants.size());
        assertEquals("John", etudiants.get(0).getNomEt());
        assertEquals("Jane", etudiants.get(1).getNomEt());
    }

    @Test
    void findById_ShouldReturnEtudiant_WhenExists() {
        long id = 1;
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");

        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.findById(id);

        assertNotNull(foundEtudiant);
        assertEquals("John", foundEtudiant.getNomEt());
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        long id = 1;

        etudiantService.deleteById(id);

        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");

        etudiantService.delete(etudiant);

        verify(etudiantRepository, times(1)).delete(etudiant);
    }
}