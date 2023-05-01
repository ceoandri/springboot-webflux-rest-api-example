package gratis.contoh.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDetail implements Serializable {

	private static final long serialVersionUID = 932043011681566790L;
	
	private String username;
	
	private String role;
	
}
