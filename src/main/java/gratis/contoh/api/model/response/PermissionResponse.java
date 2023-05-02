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
public class PermissionResponse implements Serializable {

	private static final long serialVersionUID = 755637447528208557L;

	private String module;

	private String[] permission;

}
