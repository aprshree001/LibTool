package com.library.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.library.modal.BorrowEntity;

@RepositoryRestResource(path = "borrow")
public interface BorrowRepo extends CrudRepository<BorrowEntity, String> {

	@Query(value = "SELECT b FROM BorrowEntity b WHERE b.userId = :userId")
	List<BorrowEntity> findBorrowByUserId(@Param("userId") String userId);

	@Query(value = "SELECT b FROM BorrowEntity b WHERE b.userId = :userId AND b.bookId = :bookId")
	Optional<BorrowEntity> findByUserIdAndBookId(@Param("userId") String userId, @Param("bookId") String bookId);

	@Modifying
	@Query(value = "DELETE FROM BorrowEntity b WHERE b.userId = :userId and b.bookId = :bookId")
	void removeBorrowBookFromUser(@Param("userId") String userId, @Param("bookId") String bookid);

	@Query(value = "SELECT b FROM BorrowEntity b WHERE b.userId = :userId")
	List<BorrowEntity> findByUserId(@Param("userId") String userId);

}
