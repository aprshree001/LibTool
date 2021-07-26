package com.library.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "BORROW")
public class BorrowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String borrowId;

	@Column(name = "NO_OF_BOOK")
	private Integer noOfBook;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "BOOK_ID")
	private String bookId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", updatable = false, insertable = false)
	private UserDetail user;

	@OneToOne
	@JoinColumn(name = "BOOK_ID", referencedColumnName = "BOOK_ID", updatable = false, insertable = false)
	private BookEntity book;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BorrowEntity other = (BorrowEntity) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (borrowId == null) {
			if (other.borrowId != null)
				return false;
		} else if (!borrowId.equals(other.borrowId))
			return false;
		if (noOfBook == null) {
			if (other.noOfBook != null)
				return false;
		} else if (!noOfBook.equals(other.noOfBook))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((borrowId == null) ? 0 : borrowId.hashCode());
		result = prime * result + ((noOfBook == null) ? 0 : noOfBook.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

}
