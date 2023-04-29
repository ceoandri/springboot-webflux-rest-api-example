package gratis.contoh.api.model.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MstLanguageMappingDto implements Serializable {

	private static final long serialVersionUID = 322749956269880919L;
	
	@NotNull
	private String mapping;

	@NotNull
	private String id;
	
	@NotNull
	private String en;

}
