insert into library(LIBRARY_ID,LIBRARY_NAME) VALUES (1,'INDIAN LIBRARY');
insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('120','Aparna',1,3,'priyansh','bepositive');
insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('121','Srivastav',1,5,'priyansh','beinnovative');

insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('122','Abhinav',1,5,'priyansh','becourageous');



insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('123','only one book ',1,1,'only one',' only one title');

insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('125','only one return book  ',1,1,'only one return book ',' only one  return title');


insert into book (book_id,author_name,library_id,no_of_copies,publisher,title) 
	VALUES ('126','only one book library  ',1,1,'only one library book ',' only one  library ');


insert into user(user_id,first_name,last_name,email,password) 
	values ('1','amit','kumar','amitkumar@gmail.com','$2a$10$B2n7mIOwfZJJpYJob/Mbxe9fkbendr0g1rScJPUvBDzBAuINPx.P6');

insert into user(user_id,first_name,last_name,email,password) 
	values ('2','abhijeet','kumar','abhijeet@gmail.com','$2a$10$B2n7mIOwfZJJpYJob/Mbxe9fkbendr0g1rScJPUvBDzBAuINPx.P6');


insert into user(user_id,first_name,last_name,email,password) 
	values ('3','aparna','kumar','aparna@gmail.com','$2a$10$B2n7mIOwfZJJpYJob/Mbxe9fkbendr0g1rScJPUvBDzBAuINPx.P6');


insert into limit_table (limit_id,limit_name,limit_value) values('1','MAX_BOOK_BORROW_LIMIT',2);

insert into limit_table (limit_id,limit_name,limit_value)  values('2','BOOK_COPIES_BORROW_LIMIT',1);

INSERT INTO role (id, description, name) VALUES (4, 'Admin role', 'ADMIN');
INSERT INTO role (id, description, name) VALUES (5, 'User role', 'USER');


INSERT INTO user_roles(user_id,role_id) VALUES (1,5);
INSERT INTO user_roles(user_id,role_id) VALUES (2,5);
INSERT INTO user_roles(user_id,role_id) VALUES (3,5);
