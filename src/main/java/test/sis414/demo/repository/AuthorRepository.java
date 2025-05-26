package test.sis414.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.sis414.demo.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}