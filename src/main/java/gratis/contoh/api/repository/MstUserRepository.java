package gratis.contoh.api.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import gratis.contoh.api.model.MstUser;

public interface MstUserRepository extends R2dbcRepository<MstUser, String> {

}
