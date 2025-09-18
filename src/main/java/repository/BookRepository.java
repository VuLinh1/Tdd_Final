package repository;

import model.Book;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository {
    Optional<Book> findbyId(long id);
    Book save(Book book);


}
