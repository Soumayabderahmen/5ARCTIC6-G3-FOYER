package tn.esprit.spring.services.foyer;

import tn.esprit.spring.dao.entities.Foyer;

import java.util.List;

public interface IFoyerService {
    Foyer addOrUpdate(Foyer f);

    List<Foyer> findAll();

    Foyer findById(long id);

    void deleteById(long id);

    void delete(Foyer f);

    Foyer ajoutFoyerEtBlocs(Foyer foyer);

}
