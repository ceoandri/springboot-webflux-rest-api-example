package gratis.contoh.api.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.api.model.response.BaseResponse;
import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import gratis.contoh.api.service.MstLanguageMappingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/mst-language-mapping")
@Tag(name = "Language Mapping", description = "Retrive data related language mapping")
public class MstLanguageMappingController {
	
	@Autowired
    private MstLanguageMappingService mstLanguageMappingService;
	
	@GetMapping("")
    public Mono<ResponseEntity<BaseResponse<List<MstLanguageMappingResponse>>>> getAll() {
		
        return mstLanguageMappingService.getAll()
        		.collectList()
        		.map(item -> {
        			return ResponseEntity.ok(BaseResponse.<List<MstLanguageMappingResponse>>builder()
                            .status(HttpStatus.OK.value())
                            .message("success")
                            .data(item)
                            .build());
        		});
        
    }

}
