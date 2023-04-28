package gratis.contoh.api.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.api.service.MstLanguageMappingService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/mst-language-mapping")
public class MstLanguageMappingController {
	
	@Autowired
    private MstLanguageMappingService mstLanguageMappingService;
	
	@GetMapping("")
    public Mono<String> getAll() {
        return Mono.just("Test Get Mono");
    }

}
