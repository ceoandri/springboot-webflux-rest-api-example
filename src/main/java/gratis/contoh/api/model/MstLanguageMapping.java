package gratis.contoh.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("mst_language_mapping")
@Builder
public class MstLanguageMapping implements Serializable {

	private static final long serialVersionUID = 2525814272481695461L;

	@Id
	@Column("mapping")
	@NonNull
	private String mapping;
	
	@Column("id")
	@NonNull
	private String id;
	
	@Column("en")
	@NonNull
	private String en;
	
	@Column("deleted_at")
	@Nullable
	private LocalDateTime deletedAt;

}
