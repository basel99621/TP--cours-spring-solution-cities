package monprojet.dao;

import java.util.List;

import monprojet.dto.CountryPopulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import monprojet.entity.City;
import monprojet.entity.Country;
import org.springframework.data.repository.query.Param;

// This will be AUTO IMPLEMENTED by Spring 

public interface CountryRepository extends JpaRepository<Country, Integer> {

    public Country findByName(String countryName);
    @Query("SELECT SUM(c.population) FROM City c WHERE c.country.id = :id")
    int getCountryPopulationById(Integer id);

     @Query("SELECT c.country.name as nom, SUM(c.population) as population from City c GROUP BY nom")
    List<CountryPopulation> getCountryPopulations();

}
