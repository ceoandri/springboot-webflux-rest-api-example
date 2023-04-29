package gratis.contoh.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.model.dto.MstLanguageMappingDto;
import gratis.contoh.api.model.request.MstLanguageMappingFilterRequest;
import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import gratis.contoh.api.repository.CustomMstLanguageMappingRepository;
import gratis.contoh.api.repository.MstLanguageMappingRepository;
import gratis.contoh.api.service.MstLanguageMappingService;
import gratis.contoh.api.util.mapper.ObjectMapperUtil;
import gratis.contoh.api.util.pagination.PaginationResponse;
import gratis.contoh.api.util.pagination.PaginationUtil;
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
	public Flux<MstLanguageMappingResponse> getAll() {
		ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping> mapper = 
				new ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findAllByDeletedAtIsNull()
				.map(item -> mapper.convert(MstLanguageMappingResponse.class, item));
	}

	@Override
	public Mono<MstLanguageMappingResponse> getById(String id) {
		ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping> mapper = 
				new ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findById(id)
				.filter(entity -> entity.getDeletedAt() == null)
				.map(item -> mapper.convert(MstLanguageMappingResponse.class, item));
	}

	@Override
	public Mono<MstLanguageMappingResponse> post(Mono<MstLanguageMappingDto> request) {
		ObjectMapperUtil<MstLanguageMapping, MstLanguageMappingDto> entityMapper = 
				new ObjectMapperUtil<MstLanguageMapping, MstLanguageMappingDto>();
		
		ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
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
	public Mono<MstLanguageMappingResponse> delete(String id) {
		ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
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
	public Mono<PaginationResponse<MstLanguageMappingResponse>> getPaged(
			Mono<MstLanguageMappingFilterRequest> request) {
		ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping> responseMapper = 
				new ObjectMapperUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(new Criteria(
				Operator.AND, Condition.IS_NULL, "deleted_at", new String[]{""}, ValueType.TIMESTAMP));
		
		return request
				.map(req -> PaginationUtil.getPaginationRequest(req, "mapping desc"))
				.zipWhen(paginationRequest -> {
					return Mono.just(
							(paginationRequest.getSize() * paginationRequest.getPage()) - paginationRequest.getSize());
				})
				.zipWhen(tuple -> this.customMstLanguageMappingRepository
						.findAll(criterias, tuple.getT1())
						.map(item -> responseMapper.convert(MstLanguageMappingResponse.class, item))
						.collectList())
				.zipWith(this.customMstLanguageMappingRepository.countAll(criterias))
				.map(tuple -> {
					return PaginationResponse.<MstLanguageMappingResponse>builder()
							.contents(tuple.getT1().getT2())
							.firstPage(PaginationUtil.isFirstPage(tuple.getT1().getT1().getT2()))
                            .lastPage(PaginationUtil.isLastPage(tuple.getT1().size(), tuple.getT1().getT1().getT2(), tuple.getT2()))
                            .totalPages(PaginationUtil.getTotalPages(tuple.getT1().getT2().size(), tuple.getT2()))
							.totalData(tuple.getT2())
							.build();
				});
	}

}
