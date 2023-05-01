package gratis.contoh.api.model;

import java.io.Serializable;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission implements Serializable {

	private static final long serialVersionUID = -3168522767316363247L;

	@Column("role")
	@NonNull
	private String role;
	
	@Column("module")
	@NonNull
	private String module;
	
	@Column("permission")
	@NonNull
	private String permission;
	
}
