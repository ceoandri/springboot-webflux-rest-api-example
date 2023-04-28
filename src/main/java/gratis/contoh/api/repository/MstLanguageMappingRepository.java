package gratis.contoh.api.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import gratis.contoh.api.model.MstLanguageMapping;

public interface MstLanguageMappingRepository extends R2dbcRepository<MstLanguageMapping, String> {

}
