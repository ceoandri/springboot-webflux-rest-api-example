package gratis.contoh.api.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.api.constant.AccessTypes;
import gratis.contoh.api.constant.Modules;
import gratis.contoh.api.constant.Roles;
import gratis.contoh.api.model.dto.MstLanguageMappingDto;
import gratis.contoh.api.model.request.MstLanguageMappingFilterRequest;
import gratis.contoh.api.model.response.BaseResponse;
import gratis.contoh.api.model.response.MstLanguageMappingResponse;
import gratis.contoh.api.service.MstLanguageMappingService;
import gratis.contoh.api.util.annotation.Authorize;
import gratis.contoh.api.util.pagination.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/mst-language-mapping")
public class MstLanguageMappingController {
	
	@Autowired
	private MstLanguageMappingService mstLanguageMappingService;
	
	@GetMapping("")
	@Tag(name = "Get All", description = "Retrive all data language mapping")
	public Mono<ResponseEntity<BaseResponse<List<MstLanguageMappingResponse>>>> getAll(
    		ServerHttpRequest serverHttpRequest) {
        return mstLanguageMappingService.getAll()
        		.collectList()
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<List<MstLanguageMappingResponse>>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()));
	}
	
	@GetMapping("/page")
	@Tag(name = "Get with Pagination", description = "Retrive data language mapping with pagination configuration")
	@Authorize(
			roles = {Roles.SUPER_ADMIN, Roles.ADMIN, Roles.GUEST}, 
			module = Modules.MST_LANGUAGE_MAPPING, 
			accessTypes = {AccessTypes.RETRIEVE})
	public Mono<ResponseEntity<BaseResponse<PaginationResponse<MstLanguageMappingResponse>>>> getPaged(
			ServerHttpRequest serverHttpRequest, 
			@Valid Mono<MstLanguageMappingFilterRequest> request) {
        return mstLanguageMappingService.getPaged(request)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<PaginationResponse<MstLanguageMappingResponse>>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()));
	}
	
	@GetMapping("/{id}")
	@Tag(name = "Get By ID", description = "Retrive data language mapping by specific id")
	@Authorize(
			roles = {Roles.SUPER_ADMIN, Roles.ADMIN, Roles.GUEST}, 
			module = Modules.MST_LANGUAGE_MAPPING,
			accessTypes = {AccessTypes.RETRIEVE})
	public Mono<ResponseEntity<BaseResponse<MstLanguageMappingResponse>>> getById(
    		ServerHttpRequest serverHttpRequest,
    		@PathVariable String id) {
		List<String> error = new ArrayList<String>();
		error.add(id + " is not exist");
		
        return mstLanguageMappingService.getById(id)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<MstLanguageMappingResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.NOT_FOUND.value())
        				.body(BaseResponse.<MstLanguageMappingResponse>builder()
        						.status(HttpStatus.NOT_FOUND.value())
        						.message(HttpStatus.NOT_FOUND.name())
        						.errors(error)
        						.build()));
	}
	
	@PostMapping("")
	@Tag(name = "Create/Edit", description = "create new or edit existing data language mapping")
	@Authorize(
			roles = {Roles.SUPER_ADMIN, Roles.ADMIN}, 
			module = Modules.MST_LANGUAGE_MAPPING,
			accessTypes = {AccessTypes.CREATE, AccessTypes.UPDATE})
	public Mono<ResponseEntity<BaseResponse<MstLanguageMappingResponse>>> post(
    		ServerHttpRequest serverHttpRequest,
    		@RequestBody @Valid Mono<MstLanguageMappingDto> request) {
        return mstLanguageMappingService.post(request)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<MstLanguageMappingResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()));
	}
	
	@DeleteMapping("/{id}")
	@Tag(name = "Delete", description = "soft delete existing data language mapping")
	@Authorize(
			roles = {Roles.SUPER_ADMIN, Roles.ADMIN}, 
			module = Modules.MST_LANGUAGE_MAPPING,
			accessTypes = {AccessTypes.DELETE})
	public Mono<ResponseEntity<BaseResponse<MstLanguageMappingResponse>>> delete(
    		ServerHttpRequest serverHttpRequest,
    		@PathVariable String id) {
		List<String> error = new ArrayList<String>();
		error.add(id + " is not exist");
		
        return mstLanguageMappingService.delete(id)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<MstLanguageMappingResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("item deleted")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.NOT_FOUND.value())
        				.body(BaseResponse.<MstLanguageMappingResponse>builder()
        						.status(HttpStatus.NOT_FOUND.value())
        						.message(HttpStatus.NOT_FOUND.name())
        						.errors(error)
        						.build()));
	}

}
