package gratis.contoh.api.model.dto;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MstUserDto implements Serializable {
	
	private static final long serialVersionUID = -2711046952516154133L;

	@NotNull
	private String username;
	
	@NotNull
	private String password;
	
	@NotNull
	private String role;

}
