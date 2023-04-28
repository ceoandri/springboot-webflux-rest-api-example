package gratis.contoh.api.service;

import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import reactor.core.publisher.Flux;

public interface MstLanguageMappingService {
	
	public Flux<MstLanguageMappingResponse> getAll();
	
}
