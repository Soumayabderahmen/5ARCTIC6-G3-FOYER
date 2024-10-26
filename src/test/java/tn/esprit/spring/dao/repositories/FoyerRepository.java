package tn.esprit.spring.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.entities.TypeChambre;

import java.util.List;

public interface FoyerRepository extends JpaRepository<Foyer,Long> {
    Foyer findByNomFoyer(String nom);
    // select * from Foyer where capaciteFoyer > ....
    List<Foyer> findByCapaciteFoyerGreaterThan(int capacite);
    // select * from Foyer where capaciteFoyer < ....
    List<Foyer> findByCapaciteFoyerLessThan(int capacite);
    // select * from Foyer where capaciteFoyer < .... and capaciteFoyer> ....
    List<Foyer> findByCapaciteFoyerBetween(int min, int max);
    List<Foyer> getByBlocsChambresTypeC(TypeChambre typeChambre);
}
