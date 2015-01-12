package uk.ac.ebi.spot.goci.ui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.spot.goci.ui.model.SingleNucleotidePolymorphism;


/**
 * Created by emma on 21/11/14.
 *
 * @author emma
 *         <p/>
 *         Repository accessing Single Nucloetide Polymorphism entity objectls
 */

@RepositoryRestResource
public interface SingleNucleotidePolymorphismRepository extends JpaRepository<SingleNucleotidePolymorphism, Long> {
    SingleNucleotidePolymorphism findByRsID(String rsId);

}

