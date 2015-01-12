package uk.ac.ebi.spot.goci.ui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.spot.goci.ui.model.CurationStatus;

/**
 * Created by emma on 27/11/14.
 *
 * @author emma
 *         <p/>
 *         Repository to access CurationStatus entity object
 */
@RepositoryRestResource
public interface CurationStatusRepository extends JpaRepository<CurationStatus, Long> {
}
