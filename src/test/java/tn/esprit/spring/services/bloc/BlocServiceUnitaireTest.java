package tn.esprit.spring.services.bloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc

 class BlocServiceUnitaireTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    private BlocService blocService;

    @BeforeEach
    public void setUp() {
        blocService = new BlocService(blocRepository, chambreRepository, blocRepository, foyerRepository);
    }


   @Test
   void test_addOrUpdate_ShouldSaveBloc() {
      Bloc bloc = new Bloc();
      bloc.setNomBloc("Bloc A");
      bloc.setCapaciteBloc(50);

      Bloc savedBloc = blocService.addOrUpdate(bloc);

      assertNotNull(savedBloc.getIdBloc());
      assertEquals("Bloc A", savedBloc.getNomBloc());
      assertEquals(50, savedBloc.getCapaciteBloc());
   }

     @Test
     void testFindAll() {
        // Given
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");
        blocRepository.save(bloc1);

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc B");
        blocRepository.save(bloc2);

        // When
        List<Bloc> blocs = blocService.findAll();

        // Then
        assertThat(blocs).hasSize(2);
    }

    @Test
     void testFindById() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        // When
        Bloc foundBloc = blocService.findById(savedBloc.getIdBloc());

        // Then
        assertThat(foundBloc).isNotNull();
        assertThat(foundBloc.getNomBloc()).isEqualTo("Bloc A");
    }

    @Test
     void testDeleteById() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        // When
        blocService.deleteById(savedBloc.getIdBloc());

        // Then
        assertThat(blocRepository.findById(savedBloc.getIdBloc())).isEmpty();
    }

    @Test
     void testAffecterChambresABloc() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        blocRepository.save(bloc);

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101);
        chambreRepository.save(chambre1);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102);
        chambreRepository.save(chambre2);

        // When
        blocService.affecterChambresABloc(Arrays.asList(101L, 102L), "Bloc A");

        // Then
        List<Chambre> updatedChambres = chambreRepository.findAll();
        assertThat(updatedChambres).hasSize(2);
        assertThat(updatedChambres.get(0).getBloc()).isEqualTo(bloc);
        assertThat(updatedChambres.get(1).getBloc()).isEqualTo(bloc);
    }

    @Test
     void testAffecterBlocAFoyer() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer 1");
        foyerRepository.save(foyer);

        // When
        Bloc updatedBloc = blocService.affecterBlocAFoyer(savedBloc.getNomBloc(), foyer.getNomFoyer());

        // Then
        assertThat(updatedBloc.getFoyer()).isEqualTo(foyer);
    }

    @Test
    void test_delete_ShouldDeleteBlocAndChambres() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        chambre1.setBloc(bloc);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);
        chambre2.setBloc(bloc);

        bloc.setChambres(Arrays.asList(chambre1, chambre2));
        bloc = blocRepository.save(bloc);

        blocService.delete(bloc);

        assertFalse(blocRepository.findById(bloc.getIdBloc()).isPresent());
        assertEquals(0, chambreRepository.count());
    }


}
