package gratis.contoh.api.model.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Data
public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = -774467929187742645L;

	@NotNull
    @Size(max = 30)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    @JsonProperty("remember_me")
    private Boolean rememberMe;
}
