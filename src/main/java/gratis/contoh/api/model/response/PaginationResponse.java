package gratis.contoh.api.model.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse<T> implements Serializable {

	private static final long serialVersionUID = 7681984328515916834L;
	private List<T> contents;
	private boolean firstPage;
	private boolean lastPage;
	private long totalPages;
	private long totalData;
}
