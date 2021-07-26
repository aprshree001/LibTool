package dto;

import lombok.Data;

@Data
public class BorrowRequestDto {

	private String bookid;
	private Integer quantity = new Integer(1);

}
