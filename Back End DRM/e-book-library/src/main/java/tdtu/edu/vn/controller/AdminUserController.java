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
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/admin")
//public class AdminUserController {
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
//    @PostMapping("/create-user")
//    public ResponseEntity<User> createUser(@RequestBody User newUser) {
//        newUser.setId(null);
//
//        User savedUser = userService.createUser(newUser);
//
//        if (savedUser == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(savedUser);
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//
//        if (users == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/user/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable String id) {
//        User user = userService.getUserById(id);
//
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(user);
//    }
//
//    @PostMapping("/update-user")
//    public ResponseEntity<?> updateUser(
//            @RequestParam("user") String userStr,
//            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {
//        try {
//            User updatedUser = new ObjectMapper().readValue(userStr, User.class);
//            if (avatarFile != null && !avatarFile.isEmpty()) {
//                // Xử lý lưu trữ file ở đây
//                byte[] avatarBytes = avatarFile.getBytes();
//                updatedUser.setAvatar(avatarBytes);
//            }
//
//            User savedUser = userService.updateUser(updatedUser);
//            if (savedUser == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//
//            return ResponseEntity.ok(savedUser);
//        } catch (IOException | RuntimeException e) {
//            return ResponseEntity.badRequest().body("Could not update user.");
//        }
//    }
//
//    @PostMapping("/delete-user") // should DELETE method
//    public ResponseEntity<User> deleteUser(@RequestBody User user, Principal principal) {
//        if (principal.getName().equals(user.getEmail())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        User existingUser = userService.findByEmail(user.getEmail());
//
//        if (existingUser == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (userService.deleteUser(user.getId())) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//
//
//
//}
