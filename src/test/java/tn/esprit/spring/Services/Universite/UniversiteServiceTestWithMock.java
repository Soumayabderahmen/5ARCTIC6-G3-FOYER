package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
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

    Universite universite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = Universite.builder()
                .nomUniversite("Université Test")
                .adresse("Adresse Test")
                .build();
    }

    @Test
     void testAddOrUpdate() {
        Universite universite = Universite.builder()
                .nomUniversite("Université Mock")
                .adresse("Adresse Mock")
                .build();

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertNotNull(result);
        assertEquals("Université Mock", result.getNomUniversite());
        assertEquals("Adresse Mock", result.getAdresse());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
     void testFindAll() {
        List<Universite> universites = Arrays.asList(new Universite(), new Universite());

        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Universite universite = Universite.builder().idUniversite(1L).build();

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
     void testDeleteById() {
        long id = 1L;
        when(universiteRepository.existsById(id)).thenReturn(true);
        doNothing().when(universiteRepository).deleteById(id);
        universiteService.deleteById(id);
        verify(universiteRepository, times(1)).deleteById(id);
    }

    @Test
     void testDelete() {
        Universite universiteToDelete = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("Université Test")
                .adresse("Adresse Test")

                .build();
        when(universiteRepository.existsById(universiteToDelete.getIdUniversite())).thenReturn(true);
        doNothing().when(universiteRepository).delete(universiteToDelete);
        universiteService.delete(universiteToDelete);


        verify(universiteRepository, times(1)).delete(universiteToDelete);
    }}
