package gratis.contoh.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
		
		return request
				.zipWhen(req -> Mono.just(PaginationUtil.getPaginationRequest(req, "mapping desc")))
				.zipWhen(tuple -> {
					return Mono.just(
							(tuple.getT2().getSize() * tuple.getT2().getPage()) - tuple.getT2().getSize());
				})
				.zipWhen(tuple -> {
					ArrayList<Criteria> criterias = new ArrayList<Criteria>();
					criterias.add(new Criteria(
							Operator.AND, Condition.IS_NULL, "deleted_at", new String[]{""}, ValueType.TIMESTAMP));
					return Mono.just(criterias);
				})
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
							.firstPage(PaginationUtil.isFirstPage(offset))
                            .lastPage(PaginationUtil.isLastPage(size, offset, totalData))
                            .totalPages(PaginationUtil.getTotalPages(size, totalData))
							.totalData(totalData)
							.build();
				});
	}

}
