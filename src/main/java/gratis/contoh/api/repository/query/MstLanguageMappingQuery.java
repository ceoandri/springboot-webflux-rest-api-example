package gratis.contoh.api.repository.query;

public class MstLanguageMappingQuery {

	public static final String getMstLanguageMapping = "SELECT a.mapping, a.id, a.en, a.deleted_at "
			+ "FROM mst_language_mapping a";;

}
