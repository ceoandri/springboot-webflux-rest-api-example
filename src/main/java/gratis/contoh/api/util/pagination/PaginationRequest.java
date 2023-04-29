package gratis.contoh.api.util.pagination;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest implements Serializable {
	
	private static final long serialVersionUID = -3235109097485539308L;

	@NotNull
	private Integer page;
	
	@NotNull
	private Integer size;
	
	private String sort;
}
