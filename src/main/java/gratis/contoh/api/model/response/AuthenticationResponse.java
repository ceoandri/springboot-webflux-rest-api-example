package gratis.contoh.api.model.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = -7962121504868524451L;
	
	@NotNull
	@JsonProperty("access_token")
	private String accessToken;

}
