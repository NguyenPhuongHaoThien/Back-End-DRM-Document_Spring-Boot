//package tdtu.edu.vn.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import tdtu.edu.vn.model.Category;
//import tdtu.edu.vn.service.ebook.*;
//
//import java.util.List;
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/admin")
//public class AdminCategoryController {
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
//    @PostMapping("/create-category")
//    public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
//        newCategory.setId(null);
//        Category savedCategory = categoryService.createCategory(newCategory);
//        if (savedCategory == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedCategory);
//    }
//
//    @GetMapping("/categories")
//    public ResponseEntity<List<Category>> getAllCategories() {
//        List<Category> categories = categoryService.getAllCategories();
//        if (categories == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(categories);
//    }
//
//
//    @GetMapping("/category/{id}")
//    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
//        Category category = categoryService.getCategoryById(id);
//        if (category == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(category);
//    }
//
//    @PostMapping("/update-category") // should be PATCH method
//    public ResponseEntity<Category> updateCategory(@RequestBody Category updatedCategory) {
//        Category existingCategory = categoryService.getCategoryById(updatedCategory.getId());
//        if (existingCategory == null) {
//            return ResponseEntity.notFound().build();
//        }
//        Category savedCategory = categoryService.updateCategory(updatedCategory);
//        if (savedCategory == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(savedCategory);
//    }
//
//    @PostMapping("/delete-category") // should DELETE method
//    public ResponseEntity<Category> deleteCategory(@RequestBody Category category) {
//        Category existingCategory = categoryService.getCategoryById(category.getId());
//        if (existingCategory == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (categoryService.deleteCategory(category.getId())) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//
//}
