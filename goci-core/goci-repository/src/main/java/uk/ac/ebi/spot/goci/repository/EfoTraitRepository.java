package uk.ac.ebi.spot.goci.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.spot.goci.model.EfoTrait;

import java.util.List;

/**
 * Created by emma on 04/12/14.
 *
 * @author emma
 *         <p>
 *         Repository accessing EfoTrait entity object
 */

@RepositoryRestResource
public interface EfoTraitRepository extends JpaRepository<EfoTrait, Long> {
    List<EfoTrait> findByStudiesIdAndStudiesHousekeepingPublishDateIsNotNull(Long studyId);

    List<EfoTrait> findByStudiesIdAndStudiesHousekeepingPublishDateIsNotNull(Sort sort, Long studyId);

    Page<EfoTrait> findByStudiesIdAndStudiesHousekeepingPublishDateIsNotNull(Pageable pageable, Long studyId);

    List<EfoTrait> findByAssociationsId(Long associationId);

    List<EfoTrait> findByAssociationsId(Sort sort, Long associationId);

    Page<EfoTrait> findByAssociationsId(Pageable pageable, Long associationId);
}

