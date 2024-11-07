package tn.esprit.spring.rest_controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.services.foyer.IFoyerService;

import java.util.List;

@RestController
@RequestMapping("foyer")
@AllArgsConstructor
public class FoyerRestController {
    IFoyerService service;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<?> addOrUpdate(@RequestBody Foyer f) {
        try {
            Foyer savedFoyer = service.addOrUpdate(f);
            return ResponseEntity.ok(savedFoyer);
        } catch (Exception e) {
            // Capture l'exception et renvoie une réponse d'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de l'ajout ou de la mise à jour du foyer : " + e.getMessage());
        }
    }

    @GetMapping("findAll")
    List<Foyer> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    Foyer findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    void delete(@RequestBody Foyer f) {
        service.delete(f);
    }

    @DeleteMapping("deleteById")
    void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }

}
