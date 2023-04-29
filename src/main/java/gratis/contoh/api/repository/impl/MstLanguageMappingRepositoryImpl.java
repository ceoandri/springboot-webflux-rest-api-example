package gratis.contoh.api.repository.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.repository.CustomMstLanguageMappingRepository;
import gratis.contoh.api.repository.query.MstLanguageMappingQuery;
import gratis.contoh.api.util.mapper.ObjectMapperUtil;
import gratis.contoh.api.util.pagination.PaginationRequest;
import gratis.contoh.api.util.querybuilder.Criteria;
import gratis.contoh.api.util.querybuilder.QueryBuilderUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MstLanguageMappingRepositoryImpl implements CustomMstLanguageMappingRepository {
	
	private DatabaseClient databaseClient;

    public MstLanguageMappingRepositoryImpl(DatabaseClient databaseClient) {
    	this.databaseClient = databaseClient;
	}

	@Override
	public Flux<MstLanguageMapping> findAll(ArrayList<Criteria> criterias, 
			PaginationRequest paginationRequest) {
		String base = MstLanguageMappingQuery.getMstLanguageMapping;
		String query = QueryBuilderUtil.builder(base, criterias, paginationRequest);

		ObjectMapperUtil<MstLanguageMapping, Map<String, Object>> mapper = 
				new ObjectMapperUtil<MstLanguageMapping, Map<String, Object>>();
		
		return databaseClient.sql(query)
                .fetch()
                .all()
                .flatMap(item -> Mono.just(mapper.convert(MstLanguageMapping.class, item)));
	}

	@Override
	public Mono<Long> countAll(ArrayList<Criteria> criterias) {
		String base = MstLanguageMappingQuery.getMstLanguageMapping;
		String query = QueryBuilderUtil.countBuilder(base, criterias);

		ObjectMapperUtil<Long, Object> mapper = 
				new ObjectMapperUtil<Long, Object>();
		
		return databaseClient.sql(query)
                .fetch()
                .one()
                .map(item ->  {
                	Object res = null;
                	for (Map.Entry<String,Object> entry : item.entrySet()) {
                		res = entry.getValue();
                	}
                	return mapper.convert(Long.class, res);
                });
	}

}
