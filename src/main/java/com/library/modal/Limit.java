package com.library.modal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


import lombok.Data;

@Data
@Entity
@Table(name = "LIMIT_TABLE")

public class Limit {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "LIMIT_ID")
	private String limitId;

	@Column(name = "LIMIT_NAME", unique = true)
	private String limitName;

	@Column(name = "LIMIT_VALUE")
	private Integer limitValue;

}
