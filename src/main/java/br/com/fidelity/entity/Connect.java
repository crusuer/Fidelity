package br.com.fidelity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Connect")
public class Connect {

	@Id
	@GeneratedValue
	@Column(name = "Id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser appUser;

	@ManyToOne
	@JoinColumn(name = "estab_id")
	private Estab estab;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Estab getEstab() {
		return estab;
	}

	public void setEstab(Estab estab) {
		this.estab = estab;
	}

}
