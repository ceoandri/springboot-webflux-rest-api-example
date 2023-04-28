package gratis.contoh.api.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> implements Serializable {
	
	private static final long serialVersionUID = 2715315617347577592L;
	
	private int status;
	private String message;
	private T data;
	
}
