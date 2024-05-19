//package tdtu.edu.vn.controller;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import tdtu.edu.vn.model.*;
//import tdtu.edu.vn.service.ebook.*;
//import tdtu.edu.vn.util.AESUtil;
//import tdtu.edu.vn.util.PDFSecurity;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//
//import java.util.List;
//
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/admin")
//public class AdminDocumentController {
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
//    // CRUD for document
//     @PostMapping("/create-document")
//    public ResponseEntity<Document> createDocument(@RequestBody Document newBook) {
//        newBook.setId(null);
//
//        Document savedDocument = documentService.createDocument(newBook);
//
//        if (savedDocument == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        if (savedDocument.getDrmEnabled()) {
//            String password = generatePassword();
//            EncodeDocument encodeDocument = new EncodeDocument(savedDocument.getId(), AESUtil.encrypt(password));
//            edService.createEncodeDocument(encodeDocument);
//
//            PDFSecurity.encryptPDF(savedDocument.getPdfUrl(), savedDocument.getPdfUrl(), password);
//        }
//
//        return ResponseEntity.ok(savedDocument);
//    }
//
//    @GetMapping("/documents")
//    public ResponseEntity<List<Document>> getAllDocuments() {
//        List<Document> documents = documentService.getAllDocuments();
//
//        if (documents == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(documents);
//    }
//
//    @GetMapping("/document/{id}")
//    public ResponseEntity<Document> getDocumentById(@PathVariable String id) {
//        Document document = documentService.getDocumentById(id);
//
//        if (document == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(document);
//    }
//
//    @PostMapping("/update-document")
//    public ResponseEntity<Document> updateDocument(@RequestBody Document updatedBook) {
//        Document existingDocument = documentService.getDocumentById(updatedBook.getId());
//
//
//        if (existingDocument == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (existingDocument.getDrmEnabled() != updatedBook.getDrmEnabled()) {
//            if (updatedBook.getDrmEnabled()) {
//                // Bật DRM cho tài liệu
//                String password = generatePassword();
//                EncodeDocument encodeDocument = new EncodeDocument(updatedBook.getId(), AESUtil.encrypt(password));
//                edService.createEncodeDocument(encodeDocument);
//
//                PDFSecurity.encryptPDF(updatedBook.getPdfUrl(), updatedBook.getPdfUrl(), password);
//            } else {
//                // Tắt DRM cho tài liệu
//                EncodeDocument encodeDocument = edService.findByDocumentId(updatedBook.getId());
//                if (encodeDocument != null) {
//                    edService.deleteEncodeDocument(encodeDocument.getId());
//                }
//            }
//        }
//
//        Document savedDocument = documentService.updateDocument(updatedBook);
//
//        if (savedDocument == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(savedDocument);
//    }
//
//    @PostMapping("/delete-document")
//    public ResponseEntity<Document> deleteDocument(@RequestBody Document document) {
//        Document existingDocument = documentService.getDocumentById(document.getId());
//
//        if (existingDocument == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (documentService.deleteDocument(document.getId())) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//}
