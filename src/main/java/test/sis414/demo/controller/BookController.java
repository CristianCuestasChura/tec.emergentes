

package test.sis414.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import test.sis414.demo.model.Author;
import test.sis414.demo.model.Book;
import test.sis414.demo.repository.AuthorRepository;
import test.sis414.demo.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@Tag(name = "Book ", description = "Operations related to books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books); // 200
    }

    @Operation(summary = "Get a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        logger.info("Buscando libro con ID {}", id);
        Optional<Book> optionalBook = bookRepository.findById(id);

        return optionalBook
                .map(book -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(book)) // 200
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404
    }

    @Operation(summary = "Add a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid book data")
    })
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            Author author = book.getAuthor();

            if (author.getId() == null) {
                author = authorRepository.save(author); // Guarda autor si es nuevo
            }

            book.setAuthor(author);
            Book savedBook = bookRepository.save(book);
            logger.info("Libro agregado: {}", savedBook.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook); // 201
        } catch (Exception e) {
            logger.error("Error al guardar libro", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @Operation(summary = "Delete a book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        logger.info("Eliminando libro con ID {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.ok("El libro fue eliminado correctamente"); // 200
        } else {
            logger.warn("No se encontró libro con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el libro"); // 404
        }
    }

    @Operation(summary = "Update a book completely")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            Book saved = bookRepository.save(book);
            logger.info("Libro actualizado con ID {}", id);
            return ResponseEntity.ok(saved); // 200
        } else {
            logger.warn("No se encontró libro con ID {} para actualizar", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @Operation(summary = "Partially update a book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book patched successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable Long id, @RequestBody Book partialBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (partialBook.getTitle() != null) {
                book.setTitle(partialBook.getTitle());
            }
            if (partialBook.getAuthor() != null) {
                book.setAuthor(partialBook.getAuthor());
            }
            Book saved = bookRepository.save(book);
            logger.info("Libro parcialmente actualizado con ID {}", id);
            return ResponseEntity.ok(saved); // 200
        } else {
            logger.warn("No se encontró libro con ID {} para patch", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}
