package tdtu.edu.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Author;
import tdtu.edu.vn.model.Category;
import tdtu.edu.vn.model.Document;
import tdtu.edu.vn.model.Publisher;
import tdtu.edu.vn.service.ebook.AuthorService;
import tdtu.edu.vn.service.ebook.CategoryService;
import tdtu.edu.vn.service.ebook.DocumentService;
import tdtu.edu.vn.service.ebook.PublisherService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/home")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PublisherService publisherService;

    @GetMapping()
    public Page<Document> getAllDocuments(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){
        return documentService.getAllDocuments(PageRequest.of(page, size));
    }

    @GetMapping("/documents-free")
    public Page<Document> getFreeDocuments(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){
        return documentService.getFreeDocuments(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocumentById(@PathVariable String id) {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("document", document);

        // Lấy thông tin danh mục
        Category category = categoryService.getCategoryById(document.getCategoryId());
        if (category != null) {
            response.put("category", category);
        } else {
            response.put("category", new Category()); // Tạo một đối tượng mặc định
        }

        // Lấy thông tin tác giả
        Author author = authorService.getAuthorById(document.getAuthorId());
        if (author != null) {
            response.put("author", author);
        } else {
            response.put("author", new Author()); // Tạo một đối tượng mặc định
        }

        // Lấy thông tin nhà xuất bản
        Publisher publisher = publisherService.getPublisherById(document.getPublisherId());
        if (publisher != null) {
            response.put("publisher", publisher);
        } else {
            response.put("publisher", new Publisher()); // Tạo một đối tượng mặc định
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public Page<Document> searchBooks(@RequestParam String searchTerm,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        return documentService.searchDocuments(searchTerm, PageRequest.of(page, size));
    }

    @GetMapping("/category")
    public Page<Document> getDocumentsByCategoryName(
            @RequestParam String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return documentService.findDocumentsByCategoryName(categoryName, PageRequest.of(page, size));

    }


}
