package gratis.contoh.api.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("mst_user")
@Builder
public class MstUser implements Serializable {
	
	private static final long serialVersionUID = -1920155609408875685L;

	@Id
	@Column("username")
	@NonNull
	private String username;
	
	@Column("password")
	@NonNull
	private String password;
	
	@Column("role")
	@NonNull
	private String role;

}
