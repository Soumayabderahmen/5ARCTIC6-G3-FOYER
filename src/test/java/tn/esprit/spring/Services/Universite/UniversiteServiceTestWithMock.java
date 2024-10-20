package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UniversiteServiceTestWithMock {

    @InjectMocks
    UniversiteService universiteService;

    @Mock
    UniversiteRepository universiteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrUpdate() {
        Foyer foyer = new Foyer(); // Remplir les champs requis de Foyer
        Universite universite = Universite.builder()
                .nomUniversite("Université Mock")
                .adresse("Adresse Mock")
                .foyer(foyer)
                .build();

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertNotNull(result);
        assertEquals("Université Mock", result.getNomUniversite());
        assertEquals("Adresse Mock", result.getAdresse());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testFindAll() {
        List<Universite> universites = Arrays.asList(new Universite(), new Universite());

        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Universite universite = Universite.builder().idUniversite(1L).build();

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        long id = 1L;

        doNothing().when(universiteRepository).deleteById(id);

        universiteService.deleteById(id);

        verify(universiteRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDelete() {
        Universite universite = new Universite();

        doNothing().when(universiteRepository).delete(universite);

        universiteService.delete(universite);

        verify(universiteRepository, times(1)).delete(universite);
    }
}
