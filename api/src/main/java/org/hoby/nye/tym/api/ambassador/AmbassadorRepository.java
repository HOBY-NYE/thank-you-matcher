package org.hoby.nye.tym.api.ambassador;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AmbassadorRepository extends PagingAndSortingRepository<Ambassador, Long> {
}
