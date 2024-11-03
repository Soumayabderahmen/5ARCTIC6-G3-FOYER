package tn.esprit.spring.Services.Universite;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j  // Ajout de l'annotation pour le logging
public class UniversiteService implements IUniversiteService {
     UniversiteRepository repo;

    public Universite addOrUpdate(Universite universite) {

        return repo.save(universite);

    }



    @Override
    public List<Universite> findAll() {
        List<Universite> universities = repo.findAll();
        log.info("Récupération de toutes les universités : {}", universities.size());
        return universities;
    }

    @Override
    public Universite findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Université non trouvée avec l'ID " + id));
    }

    @Override
    public void deleteById(long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Impossible de supprimer, l'Université avec l'ID " + id + " n'existe pas.");
        }
        repo.deleteById(id);
        log.info("Université avec l'ID {} supprimée.", id);
    }

    @Override
    public void delete(Universite u) {
        // Vérifier si l'université existe avant de supprimer
        if (u == null || !repo.existsById(u.getIdUniversite())) {
            throw new IllegalArgumentException("L'Université à supprimer n'existe pas.");
        }
        repo.delete(u);
        log.info("Université supprimée : {}", u);
    }
}
