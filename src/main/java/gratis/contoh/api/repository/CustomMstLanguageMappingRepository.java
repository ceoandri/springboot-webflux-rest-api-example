package gratis.contoh.api.repository;

import java.util.ArrayList;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.util.pagination.PaginationRequest;
import gratis.contoh.api.util.querybuilder.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomMstLanguageMappingRepository {

	Flux<MstLanguageMapping> findAll(ArrayList<Criteria> criterias, 
			PaginationRequest paginationRequest);
	
	Mono<Long> countAll(ArrayList<Criteria> criterias);

}
