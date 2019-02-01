package br.com.fidelity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Estab")
public class Estab {

	@Id
	@GeneratedValue
	@Column(name = "Estab_Id", nullable = false)
	private Long estabId;

	@Column(name = "Nome", length = 200, nullable = false)
	private String nome;

	public Long getEstabId() {
		return estabId;
	}

	public void setEstabId(Long estabId) {
		this.estabId = estabId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
