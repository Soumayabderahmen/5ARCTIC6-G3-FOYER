package tn.esprit.spring.Services.Foyer;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import static org.mockito.Mockito.when;

import java.util.List;

 class FoyerServiceTestTest  {
    @InjectMocks
    private FoyerService foyerService;

    @Mock
    private FoyerRepository foyerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   //* @Test
   // void addOrUpdate_ShouldSaveFoyer()
   // {
   //     Foyer foyer = new Foyer();
   //     foyer.setNomFoyer("Foyer Test");
   //     when(foyerRepository.save(foyer)).thenReturn(foyer);
   //     Foyer savedFoyer = foyerService.addOrUpdate(foyer);
   //     assertNotNull(savedFoyer);
   // }

}
