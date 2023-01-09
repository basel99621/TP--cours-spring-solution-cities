package monprojet.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import monprojet.entity.City;
import monprojet.entity.Country;

@Log4j2 // Génère le 'logger' pour afficher les messages de trace
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CountryRepository countryDAO;
    @Autowired
    private CityRepository cityDAO;

    @Test
    void onTrouveLePaysDesVilles() {
        log.info("On vérifie que les pays des villes sont bien récupérés");
        City paris = cityDAO.findByName("Paris");
        Country france = countryDAO.findById(1).orElseThrow();
        assertEquals(france, paris.getCountry(), "Paris est en France");
    }

    @Test
    void onTrouveLesVillesDesPays() {
        log.info("On vérifie que les villes d'un pays sont accessibles");
        City paris = cityDAO.findByName("Paris");
        Country france = countryDAO.findById(1).orElseThrow();
        assertTrue( france.getCities().contains(paris), "France contient Paris");
    }

    @Test
    // L'entity "City" définit une contrainte sur la taille minimum du nom des villes
    void onVerifieLesContraintesDeValidation() {
        log.info("On vérifie que Spring honore les contraintes de validation");
        City city = new City();
        city.setName("P"); // Ce nom est trop court,  @Size(min = 2) dans City.java
        city.setPopulation(1000000);
        city.setCountry(countryDAO.findById(1).orElseThrow());
        try {
            cityDAO.save(city);
            fail("On doit avoir une violation de contrainte d'intégrité");
        } catch (ConstraintViolationException e) {
            log.info("On a reçu l'exception : " + e.getMessage());
        }    
    }

}
