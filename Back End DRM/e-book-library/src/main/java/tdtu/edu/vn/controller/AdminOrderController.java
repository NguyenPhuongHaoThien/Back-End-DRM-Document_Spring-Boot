//package tdtu.edu.vn.controller;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import tdtu.edu.vn.model.ActivationCode;
//import tdtu.edu.vn.model.Document;
//import tdtu.edu.vn.model.Order;
//import tdtu.edu.vn.model.User;
//import tdtu.edu.vn.service.ebook.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//@RestController
//@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/admin")
//public class AdminOrderController {
//
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
//    // CRUD for Order
//    @PostMapping("/create-order")
//    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
//        newOrder.setId(null);
//        newOrder.setOrderDate(new Date());
//        Order savedOrder = orderService.createOrder(newOrder);
//
//        if (savedOrder == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(savedOrder);
//    }
//
//    @GetMapping("/orders")
//    public ResponseEntity<List<AdminController.OrderResponse>> getAllOrders() {
//        List<AdminController.OrderResponse> responses = new ArrayList<>();
//        List<Order> orders = orderService.getAllOrders();
//
//        if (orders == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        for (Order order : orders) {
//            User user = userService.getUserById(order.getUserId());
//            List<Document> documents = new ArrayList<>();
//
//            for (String bookId : order.getBookIds()) {
//                try {
//                    documents.add(documentService.getDocumentById(bookId));
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//            ActivationCode activationCode = null;
//            if (order.getActivationCodeId() != null) {
//                activationCode = activationCodeService.getActivationCodeById(order.getActivationCodeId());
//            }
//            responses.add(new AdminController.OrderResponse(order, user, documents, activationCode));
//        }
//
//        return ResponseEntity.ok(responses);
//    }
//
//    @GetMapping("/order/{id}")
//    public ResponseEntity<AdminController.OrderResponse> getOrderById(@PathVariable String id) {
//        Order order = orderService.getOrderById(id);
//
//        if (order == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        User user = userService.getUserById(order.getUserId());
//        List<Document> documents = new ArrayList<>();
//
//        for (String bookId : order.getBookIds()) {
//            try {
//                documents.add(documentService.getDocumentById(bookId));
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        ActivationCode activationCode = null;
//        if (order.getActivationCodeId() != null) {
//            activationCode = activationCodeService.getActivationCodeById(order.getActivationCodeId());
//        }
//
//        return ResponseEntity.ok(new AdminController.OrderResponse(order, user, documents, activationCode));
//    }
//
//    @PostMapping("/update-order") // should be PATCH
//    public ResponseEntity<Order> updateOrder(@RequestBody Order updatedOrder) {
//        Order existingOrder = orderService.getOrderById(updatedOrder.getId());
//
//        if (existingOrder == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        updatedOrder.setOrderDate(existingOrder.getOrderDate());
//        Order savedOrder = orderService.updateOrder(updatedOrder);
//
//
//        if (savedOrder == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok(savedOrder);
//    }
//
//    @PostMapping("/delete-order")
//    public ResponseEntity<Order> deleteOrder(@RequestBody Order order) {
//        Order existingOrder = orderService.getOrderById(order.getId());
//
//        if (existingOrder == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        if (orderService.deleteOrder(order)) {
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
// @PostMapping("/accept-order")
//    public ResponseEntity<Order> acceptOrder(@RequestBody Order order) {
//        Order existingOrder = orderService.getOrderById(order.getId());
//
//        if (existingOrder == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        existingOrder.setOrderStatus(Order.OrderStatus.ACCEPTED);
//        orderService.updateOrder(existingOrder);
//
//        ActivationCode activationCode = activationCodeService.getActivationCodeById(existingOrder.getActivationCodeId());
//        activationCode.setStatus(ActivationCode.ActivationCodeStatus.USED);
//        activationCodeService.updateActivationCode(activationCode);
//
//        // send activation code to user's email
//        try {
//            User user = userService.getUserById(existingOrder.getUserId());
//            String subject = "Activation code for your order";
//            String body = "Your activation code is: " + activationCode.getCode();
//            emailService.sendEmail("nguyntrungkin091@gmail.com", user.getEmail(), subject, body);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        return ResponseEntity.ok(existingOrder);
//    }
//
//    // cancel order (delete activation code)
//    @PostMapping("/cancel-order")
//    public ResponseEntity<Order> cancelOrder(@RequestBody Order order) {
//        Order existingOrder = orderService.getOrderById(order.getId());
//
//        if (existingOrder == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        existingOrder.setOrderStatus(Order.OrderStatus.CANCELLED);
//        orderService.updateOrder(existingOrder);
//
//        ActivationCode activationCode = activationCodeService.getActivationCodeById(existingOrder.getActivationCodeId());
//        activationCodeService.deleteActivationCode(activationCode.getId());
//
//        return ResponseEntity.ok(existingOrder);
//    }
//
//
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Data
//    public static class OrderResponse {
//        Order order;
//        User user;
//        List<Document> documents;
//        ActivationCode activationCode;
//    }
//
//}
