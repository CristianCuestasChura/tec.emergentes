

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
import test.sis414.demo.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
@Tag(name = "Author ", description = "Operations related to authors")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Operation(summary = "Get all authors")
    @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return ResponseEntity.ok(authors); // 200
    }

    @Operation(summary = "Get author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        logger.info("Buscando autor con ID {}", id);
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        return optionalAuthor
                .map(author -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(author)) // 200
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404
    }

    @Operation(summary = "Add a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) {
        try {
            Author savedAuthor = this.authorRepository.save(author);
            logger.info("Autor agregado: {}", savedAuthor.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor); // 201
        } catch (Exception e) {
            logger.error("Error al crear autor", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        logger.info("Eliminando autor con ID {}", id);
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return ResponseEntity.ok("El autor fue eliminado correctamente"); // 200
        } else {
            logger.warn("No se encontró autor con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el autor"); // 404
        }
    }

    @Operation(summary = "Update an author completely")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(updatedAuthor.getName());
            Author saved = authorRepository.save(author);
            logger.info("Autor actualizado con ID {}", id);
            return ResponseEntity.ok(saved); // 200
        } else {
            logger.warn("No se encontró autor con ID {} para actualizar", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @Operation(summary = "Partially update an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author patched successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Author> patchAuthor(@PathVariable Long id, @RequestBody Author partialAuthor) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            if (partialAuthor.getName() != null) {
                author.setName(partialAuthor.getName());
            }
            Author saved = authorRepository.save(author);
            logger.info("Autor parcialmente actualizado con ID {}", id);
            return ResponseEntity.ok(saved); // 200
        } else {
            logger.warn("No se encontró autor con ID {} para patch", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}

