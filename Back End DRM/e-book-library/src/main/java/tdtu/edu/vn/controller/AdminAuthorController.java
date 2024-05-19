//package tdtu.edu.vn.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import tdtu.edu.vn.model.Author;
//import tdtu.edu.vn.service.ebook.*;
//
//import java.util.List;
//
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/admin")
//public class AdminAuthorController {
//    private AuthorService authorService;
//    private CategoryService categoryService;
//    private DocumentService documentService;
//    private PublisherService publisherService;
//    private ActivationCodeService activationCodeService;
//    private UserService userService;
//    private OrderService orderService;
//    private BCryptPasswordEncoder passwordEncoder;
//    private EncodeDocumentService edService;
//    private EmailService emailService;
//
//    // CRUD for author
//    @PostMapping("/create-author")
//    public ResponseEntity<Author> createAuthor(@RequestBody Author newAuthor) {
//        newAuthor.setId(null);
//        Author savedAuthor = authorService.createAuthor(newAuthor);
//        if (savedAuthor == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedAuthor);
//    }
//
//    @GetMapping("/authors")
//    public ResponseEntity<List<Author>> getAllAuthors() {
//        List<Author> authors = authorService.getAllAuthors();
//        if (authors == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(authors);
//    }
//
//    @GetMapping("/author/{id}")
//    public ResponseEntity<Author> getAuthorById(@PathVariable String id) {
//        Author author = authorService.getAuthorById(id);
//        if (author == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(author);
//    }
//
//    @PostMapping("/update-author") // should be PATCH method
//    public ResponseEntity<Author> updateAuthor(@RequestBody Author updatedAuthor) {
//        Author existingAuthor = authorService.getAuthorById(updatedAuthor.getId());
//        if (existingAuthor == null) {
//            return ResponseEntity.notFound().build();
//        }
//        Author savedAuthor = authorService.updateAuthor(updatedAuthor);
//        if (savedAuthor == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedAuthor);
//    }
//
//    @PostMapping("/delete-author") // should DELETE method
//    public ResponseEntity<Author> deleteAuthor(@RequestBody Author author) {
//        Author existingAuthor = authorService.getAuthorById(author.getId());
//        if (existingAuthor == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (authorService.deleteAuthor(author.getId())) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//}
