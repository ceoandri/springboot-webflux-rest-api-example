package gratis.contoh.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import gratis.contoh.api.repository.MstLanguageMappingRepository;
import gratis.contoh.api.service.MstLanguageMappingService;
import gratis.contoh.api.util.ObjectUtil;
import reactor.core.publisher.Flux;

@Service
public class MstLanguageMappingServiceImpl implements MstLanguageMappingService {
	
	@Autowired
	private MstLanguageMappingRepository mstLanguageMappingRepository;

	@Override
	public Flux<MstLanguageMappingResponse> getAll() {
		ObjectUtil<MstLanguageMappingResponse, MstLanguageMapping> mapper = 
				new ObjectUtil<MstLanguageMappingResponse, MstLanguageMapping>();
		
		return this.mstLanguageMappingRepository.findAll()
				.map(item -> mapper.convert(MstLanguageMappingResponse.class, item));
	}

}
