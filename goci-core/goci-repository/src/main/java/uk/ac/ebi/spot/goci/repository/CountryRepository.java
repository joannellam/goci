package uk.ac.ebi.spot.goci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.spot.goci.model.Country;

/**
 * Created by emma on 19/12/14.
 * @author emma
 *
 * Repository accessing Country entity objects
 */
@RepositoryRestResource
public interface CountryRepository extends JpaRepository<Country, Long> {
}
