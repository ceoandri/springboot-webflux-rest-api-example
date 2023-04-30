package gratis.contoh.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.model.dto.MstLanguageMappingDto;
import gratis.contoh.api.model.request.MstLanguageMappingFilterRequest;
import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import gratis.contoh.api.repository.CustomMstLanguageMappingRepository;
import gratis.contoh.api.repository.MstLanguageMappingRepository;
import gratis.contoh.api.service.MstLanguageMappingService;
import gratis.contoh.api.util.mapper.ObjectMapper;
import gratis.contoh.api.util.pagination.PaginationRequest;
import gratis.contoh.api.util.pagination.PaginationResponse;
import gratis.contoh.api.util.pagination.Pagination;
import gratis.contoh.api.util.querybuilder.Condition;
import gratis.contoh.api.util.querybuilder.Criteria;
import gratis.contoh.api.util.querybuilder.Operator;
import gratis.contoh.api.util.querybuilder.ValueType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MstLanguageMappingServiceImpl implements MstLanguageMappingService {
	
	@Autowired
	private MstLanguageMappingRepository mstLanguageMappingRepository;
	
	@Autowired
	private CustomMstLanguageMappingRepository customMstLanguageMappingRepository;

	@Override
	@Transactional
	@Cacheable
	public Flux<MstLanguageMappingResponse> getAll() {
		ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping> mapper = 
				new ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findAllByDeletedAtIsNull()
				.map(item -> mapper.convert(MstLanguageMappingResponse.class, item)).cache();
	}

	@Override
	@Transactional
	public Mono<MstLanguageMappingResponse> getById(String id) {
		ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping> mapper = 
				new ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findById(id)
				.filter(entity -> entity.getDeletedAt() == null)
				.map(item -> mapper.convert(MstLanguageMappingResponse.class, item));
	}

	@Override
	@Transactional
	public Mono<MstLanguageMappingResponse> post(Mono<MstLanguageMappingDto> request) {
		ObjectMapper<MstLanguageMapping, MstLanguageMappingDto> entityMapper = 
				new ObjectMapper<MstLanguageMapping, MstLanguageMappingDto>();
		
		ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return request
				.map(dto -> entityMapper.convert(MstLanguageMapping.class, dto))
				.map(entity -> {
					entity.setDeletedAt(null);
					return entity;
				})
				.flatMap(entity -> this.mstLanguageMappingRepository.save(entity))
				.map(entity -> responseMapper.convert(MstLanguageMappingResponse.class, entity));
	}

	@Override
	@Transactional
	public Mono<MstLanguageMappingResponse> delete(String id) {
		ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findById(id)
				.filter(entity -> entity.getDeletedAt() == null)
				.map(entity -> {
					entity.setDeletedAt(LocalDateTime.now());
					return entity;
				})
				.flatMap(entity -> this.mstLanguageMappingRepository.save(entity))
				.map(entity -> responseMapper.convert(MstLanguageMappingResponse.class, entity));
	}

	@Override
	@Transactional
	public Mono<PaginationResponse<MstLanguageMappingResponse>> getPaged(
			Mono<MstLanguageMappingFilterRequest> request) {
		ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapper<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return request
				.zipWhen(req -> Mono.just(Pagination.getPaginationRequest(req, "mapping desc")))
				.zipWhen(tuple -> Mono.just(getOffset(tuple.getT2())))
				.zipWhen(tuple -> Mono.just(generateCriteria(tuple.getT1().getT1())))
				.zipWhen(tuple -> this.customMstLanguageMappingRepository
						.findAll(tuple.getT2(), tuple.getT1().getT1().getT2())
						.map(item -> responseMapper.convert(MstLanguageMappingResponse.class, item))
						.collectList())
				.zipWhen(tuple -> this.customMstLanguageMappingRepository.countAll(tuple.getT1().getT2()))
				.map(tuple -> {
					int size = tuple.getT1().getT1().getT1().getT1().getT2().getSize();
					int offset = tuple.getT1().getT1().getT1().getT2();
					long totalData = tuple.getT2();
					
					return PaginationResponse.<MstLanguageMappingResponse>builder()
							.contents(tuple.getT1().getT2())
							.firstPage(Pagination.isFirstPage(offset))
                            .lastPage(Pagination.isLastPage(size, offset, totalData))
                            .totalPages(Pagination.getTotalPages(size, totalData))
							.totalData(totalData)
							.build();
				});
	}
	
	private int getOffset(PaginationRequest req) {
		return (req.getSize() * req.getPage()) - req.getSize();
	}
	
	private ArrayList<Criteria> generateCriteria(MstLanguageMappingFilterRequest req) {
		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(new Criteria(
				Operator.AND, 
				Condition.IS_NULL, 
				"deleted_at", 
				new String[]{""}, 
				ValueType.TIMESTAMP));
		
		if (req.getMapping() != null && !req.getMapping().isEmpty() && !req.getMapping().isBlank()) {
			criterias.add(new Criteria(
					Operator.AND, 
					Condition.LIKE, 
					"LOWER(mapping)", 
					new String[]{req.getMapping().toLowerCase()}, 
					ValueType.TEXT));
		}
		
		if (req.getId() != null && !req.getId().isEmpty() && !req.getId().isBlank()) {
			criterias.add(new Criteria(
					Operator.AND, 
					Condition.LIKE, 
					"LOWER(id)", 
					new String[]{req.getId().toLowerCase()}, 
					ValueType.TEXT));
		}
		
		if (req.getEn() != null && !req.getEn().isEmpty() && !req.getEn().isBlank()) {
			criterias.add(new Criteria(
					Operator.AND, 
					Condition.LIKE, 
					"LOWER(en)", 
					new String[]{req.getEn().toLowerCase()}, 
					ValueType.TEXT));
		}
		
		return criterias;
	}

}
