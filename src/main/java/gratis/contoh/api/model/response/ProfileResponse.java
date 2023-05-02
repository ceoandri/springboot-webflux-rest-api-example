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
public class ProfileResponse implements Serializable {

	private static final long serialVersionUID = -5925312552305334792L;

	private String username;
	
	private String role;

}
