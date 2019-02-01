package br.com.fidelity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.fidelity.entity.Estab;

public interface EstabRepository extends CrudRepository<Estab, Long>{

	@Query("SELECT e FROM Connect c RIGHT JOIN c.estab e LEFT JOIN c.appUser a WHERE c.estab IS NULL AND c.appUser IS NULL")
    public List<Estab> findNotConnected(@Param("userName") String userName);
}
