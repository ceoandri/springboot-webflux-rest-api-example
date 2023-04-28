package gratis.contoh.api.service;

import gratis.contoh.api.model.MstLanguageMapping;
import reactor.core.publisher.Flux;

public interface MstLanguageMappingService {
	
	public Flux<MstLanguageMapping> getAll();
	
}
