package tn.esprit.spring.services.bloc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class BlocServiceTest {

    @InjectMocks
    private BlocService blocService;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    private Bloc bloc;
    private Chambre chambre;
    private Foyer foyer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");

        bloc = new Bloc();
        bloc.setNomBloc("Bloc1");

        chambre = new Chambre();
        chambre.setNumeroChambre(101);
        chambre.setBloc(bloc);
        bloc.setChambres(Arrays.asList(chambre));
    }

    @Test
    void testAddOrUpdate() {
        // Mocking the behavior
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
        verify(blocRepository).save(bloc);
        verify(chambreRepository).save(chambre);
    }

    @Test
    void testFindAll() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        List<Bloc> result = blocService.findAll();

        assertEquals(1, result.size());
        assertEquals("Bloc1", result.get(0).getNomBloc());
    }

    @Test
    void testFindById() {
        when(blocRepository.findById(anyLong())).thenReturn(Optional.of(bloc));

        Bloc result = blocService.findById(1L);

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
    }

    @Test
    void testDeleteById() {
        doNothing().when(blocRepository).deleteById(anyLong());

        blocService.deleteById(1L);

        verify(blocRepository).deleteById(1L);
    }

    @Test
    void testAffecterChambresABloc() {
        when(blocRepository.findByNomBloc(anyString())).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(anyLong())).thenReturn(chambre);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc result = blocService.affecterChambresABloc(Arrays.asList(101L), "Bloc1");

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
        verify(chambreRepository).save(chambre);
    }

    @Test
    void testAffecterBlocAFoyer() {
        when(blocRepository.findByNomBloc(anyString())).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer(anyString())).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer("Bloc1", "Foyer1");

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
        assertEquals("Foyer1", result.getFoyer().getNomFoyer());
        verify(blocRepository).save(bloc);
    }
}