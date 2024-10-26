package tn.esprit.spring.services.foyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.*;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    FoyerRepository repo;
    BlocRepository blocRepository;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return repo.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return repo.findAll();
    }

    @Override
    public Foyer findById(long id) {
        Optional<Foyer> optionalFoyer = repo.findById(id);
        if (optionalFoyer.isPresent()) {
            return optionalFoyer.get();
        } else {
            throw new NoSuchElementException("Foyer not found with id: " + id);
        }
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Foyer f) {
        repo.delete(f);
    }




    @Override
    public Foyer ajoutFoyerEtBlocs(Foyer foyer) {

        //-----------------------------------------
        List<Bloc> blocs = foyer.getBlocs();
        foyer = repo.save(foyer);
        for (Bloc b : blocs) {
            b.setFoyer(foyer);
            blocRepository.save(b);
        }
        return foyer;
    }

}
