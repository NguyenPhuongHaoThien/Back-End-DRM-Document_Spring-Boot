//package tdtu.edu.vn.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import tdtu.edu.vn.model.Publisher;
//import tdtu.edu.vn.service.ebook.*;
//
//import java.util.List;
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class AdminPublisherController {
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
//    // CRUD for Publisher
//    @PostMapping("/create-publisher")
//    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher newPublisher) {
//        newPublisher.setId(null);
//        Publisher savedPublisher = publisherService.createPublisher(newPublisher);
//        if (savedPublisher == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedPublisher);
//    }
//
//    @GetMapping("/publishers")
//    public ResponseEntity<List<Publisher>> getAllPublishers() {
//        List<Publisher> publishers = publisherService.getAllPublishers();
//        if (publishers == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(publishers);
//    }
//
//    @GetMapping("/publisher/{id}")
//    public ResponseEntity<Publisher> getPublisherById(@PathVariable String id) {
//        Publisher publisher = publisherService.getPublisherById(id);
//        if (publisher == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(publisher);
//    }
//
//    @PostMapping("/update-publisher") // should be PATCH method
//    public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher updatedPublisher) {
//        Publisher existingPublisher = publisherService.getPublisherById(updatedPublisher.getId());
//        if (existingPublisher == null) {
//            return ResponseEntity.notFound().build();
//        }
//        Publisher savedPublisher = publisherService.updatePublisher(updatedPublisher);
//        if (savedPublisher == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedPublisher);
//    }
//
//
//    @PostMapping("/delete-publisher") // should DELETE method
//    public ResponseEntity<Publisher> deletePublisher(@RequestBody Publisher publisher) {
//        Publisher existingPublisher = publisherService.getPublisherById(publisher.getId());
//        if (existingPublisher == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (publisherService.deletePublisher(publisher.getId())) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//
//}
