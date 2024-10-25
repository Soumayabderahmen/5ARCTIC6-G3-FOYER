package tn.esprit.spring.Services.Chambre;

import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.util.List;

public interface IChambreService {
    Chambre addOrUpdate(Chambre c);
    List<Chambre> findAll();
    Chambre findById(long id);
    void deleteById(long id);
    void delete(Chambre c);
    List<Chambre>  getChambresParNomBloc( String nomBloc);
    void listeChambresParBloc();
    void pourcentageChambreParTypeChambre();
    long nbChambreParTypeEtBloc(TypeChambre type, long idBloc);

}
