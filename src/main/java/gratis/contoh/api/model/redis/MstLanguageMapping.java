package gratis.contoh.api.model.redis;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MstLanguageMapping implements Serializable {

	private static final long serialVersionUID = 7769745339658744124L;

	private String mapping;

	private String id;
	
	private String en;
	
}
