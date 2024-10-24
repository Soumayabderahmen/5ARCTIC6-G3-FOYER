package tn.esprit.spring.RestControllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantRestControllerTest {

    @InjectMocks
    private EtudiantRestController controller;

    @Mock
    private IEtudiantService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testAddOrUpdate() {
        Etudiant etudiant = new Etudiant();
        when(service.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = controller.addOrUpdate(etudiant);

        assertEquals(etudiant, result);
        verify(service).addOrUpdate(etudiant);
    }

    @Test
     void testFindAll() {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);
        when(service.findAll()).thenReturn(etudiants);

        List<Etudiant> result = controller.findAll();

        assertEquals(2, result.size());
        verify(service).findAll();
    }

    @Test
     void testFindById() {
        Etudiant etudiant = new Etudiant();
        when(service.findById(1L)).thenReturn(etudiant);

        Etudiant result = controller.findById(1L);

        assertEquals(etudiant, result);
        verify(service).findById(1L);
    }

    @Test
     void testDelete() {
        Etudiant etudiant = new Etudiant();
        doNothing().when(service).delete(any(Etudiant.class));

        controller.delete(etudiant);

        verify(service).delete(etudiant);
    }

    @Test
     void testDeleteById() {
        doNothing().when(service).deleteById(1L);

        controller.deleteById(1L);

        verify(service).deleteById(1L);
    }
}