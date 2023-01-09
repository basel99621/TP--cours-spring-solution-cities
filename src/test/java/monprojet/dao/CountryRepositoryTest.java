package monprojet.dao;

import monprojet.dto.CountryPopulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import monprojet.entity.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@Log4j2 // Génère le 'logger' pour afficher les messages de trace
@DataJpaTest
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryDAO;

    @Test
    void lesNomsDePaysSontTousDifferents() {
        log.info("On vérifie que les noms de pays sont tous différents ('unique') dans la table 'Country'");
        
        Country paysQuiExisteDeja = new Country("XX", "France");
        try {
            countryDAO.save(paysQuiExisteDeja); // On essaye d'enregistrer un pays dont le nom existe   

            fail("On doit avoir une violation de contrainte d'intégrité (unicité)");
        } catch (DataIntegrityViolationException e) {
            // Si on arrive ici c'est normal, l'exception attendue s'est produite
        }
    }

    @Test
    @Sql("test-data.sql") // On peut charger des donnnées spécifiques pour un test
    void onSaitCompterLesEnregistrements() {
        log.info("On compte les enregistrements de la table 'Country'");
        int combienDePaysDansLeJeuDeTest = 3 + 1; // 3 dans data.sql, 1 dans test-data.sql
        long nombre = countryDAO.count();
        assertEquals(combienDePaysDansLeJeuDeTest, nombre, "On doit trouver 4 pays" );
    }

    @Test
    void calculDeLaPopulationAPartirdeID() {
        log.info("On vérifie que la population calculée est bien correcte ");
        int idTest = 2;// PAYS = United Kingdom, Population = 18


        int population = countryDAO.getCountryPopulationById(idTest);
        assertEquals(18 , population, "On doit trouver 18 habitants" );



    }

    @Test
    void testListePaysPopulation() {
        log.info("On vérifie que le mapping population et pays est respecté");


        List<CountryPopulation> list = countryDAO.getCountryPopulations();

        assertEquals(3, list.size(), "On doit trouver 12 habitants" );

        List<String> nomDesPays = new ArrayList<>();
        nomDesPays.add( "France");
        nomDesPays.add( "United Kingdom");
        nomDesPays.add( "United States of America");

        List<Integer>populationDesPays = new ArrayList<>();
        populationDesPays.add( 12);
        populationDesPays.add( 18);
        populationDesPays.add( 27);

        for(int i = 0; i <list.size(); i++) {

            assertEquals(nomDesPays.get(i), list.get(i).getName(), "Les noms des pays doivent correspondre" );
            assertEquals(populationDesPays.get(i), list.get(i).getPopulation(), "Les populations des pays doivent correspondre" );
        }



    }

}
