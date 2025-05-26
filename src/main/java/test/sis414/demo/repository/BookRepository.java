package test.sis414.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import test.sis414.demo.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
