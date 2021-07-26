package com.library.modal;

import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="LIBRARY")
public class Library {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="LIBRARY_ID")
	private Integer libId;
	
    @OneToMany(mappedBy ="lib" )
	List<BookEntity> books;
    
    
    @Column(name="LIBRARY_NAME")
    private String libraryname;
	
	
}
