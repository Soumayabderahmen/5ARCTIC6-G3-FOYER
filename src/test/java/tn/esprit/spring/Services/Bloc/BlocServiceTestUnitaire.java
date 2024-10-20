package tn.esprit.spring.Services.Bloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureMockMvc
public class BlocServiceTestUnitaire {

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
    public void testAddOrUpdate() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101);
        chambre1.setBloc(bloc);

        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102);
        chambre2.setBloc(bloc);

        bloc.setChambres(Arrays.asList(chambre1, chambre2));

        // When
        Bloc savedBloc = blocService.addOrUpdate(bloc);

        // Then
        assertThat(savedBloc.getNomBloc()).isEqualTo("Bloc A");
        assertThat(savedBloc.getChambres()).hasSize(2);
    }

    @Test
    public void testFindAll() {
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
    public void testFindById() {
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
    public void testDeleteById() {
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
    public void testAffecterChambresABloc() {
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
    public void testAffecterBlocAFoyer() {
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
    public void testDelete() {
        // Given
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        Bloc savedBloc = blocRepository.save(bloc);

        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101);
        chambre1.setBloc(savedBloc);
        chambreRepository.save(chambre1);

        // When
        blocService.delete(savedBloc);

        // Then
        assertThat(blocRepository.findById(savedBloc.getIdBloc())).isEmpty();
        assertThat(chambreRepository.findById(chambre1.getIdChambre())).isEmpty();
    }
}
