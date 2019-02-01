package br.com.fidelity.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.fidelity.entity.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long>{
	AppUser findByUserName(String userName);

}
