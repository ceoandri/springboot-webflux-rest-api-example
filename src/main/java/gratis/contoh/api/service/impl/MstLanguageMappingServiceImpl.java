package gratis.contoh.api.service.impl;

import org.springframework.stereotype.Service;

import gratis.contoh.api.model.MstLanguageMapping;
import gratis.contoh.api.service.MstLanguageMappingService;
import reactor.core.publisher.Flux;

@Service
public class MstLanguageMappingServiceImpl implements MstLanguageMappingService {

	@Override
	public Flux<MstLanguageMapping> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
