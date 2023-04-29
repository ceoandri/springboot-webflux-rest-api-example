package gratis.contoh.api.model.request;

import gratis.contoh.api.util.pagination.PaginationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class MstLanguageMappingFilterRequest extends PaginationRequest {
	
	private static final long serialVersionUID = -750331641604817077L;
	
	private String mapping;
	private String id;
	private String en;

}
