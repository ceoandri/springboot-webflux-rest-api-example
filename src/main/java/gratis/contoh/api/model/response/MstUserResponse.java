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
public class MstUserResponse implements Serializable {
	
	private static final long serialVersionUID = -7292805850648782521L;

	private String username;
	
	private String role;

}
