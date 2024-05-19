package tdtu.edu.vn.controller;

import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.*;
import tdtu.edu.vn.repository.ReadingHistoryRepository;
import tdtu.edu.vn.service.ebook.*;
import tdtu.edu.vn.util.AESUtil;
import tdtu.edu.vn.util.JwtUtilsHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static tdtu.edu.vn.util.PDFSecurity.openEncryptedPdf;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("")
@AllArgsConstructor
public class ReadController {
    private final DocumentService documentService;
    private ActivationCodeService activationCodeService;
    private UserService userService;
    private JwtUtilsHelper jwtUtilsHelper;
    private OrderService orderService;
    private EncodeDocumentService edService;
    private ReadingHistoryRepository readingHistoryRepository;
    @PostMapping("/read/{id}/pdf")
    public ResponseEntity<Resource> getDocumentPdf(
            @PathVariable String id,
            @RequestParam(required = false) Integer page,
            @RequestBody(required = false) ActivationCode activationCode,
            HttpServletRequest request) {
        // Lấy token từ header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Lấy thông tin người dùng từ token
        String email = jwtUtilsHelper.getEmailFromToken(token);
        String role = jwtUtilsHelper.getRoleFromToken(token);
        User user = userService.findByEmail(email);
        String userId = user.getId();

        if(role.equals("ROLE_ADMIN")){
            return serveDocument(documentService.getDocumentById(id));
        }

        Document document = documentService.getDocumentById(id);
        if (!document.getDrmEnabled()) {
            //tài liệu không phải DRM
            return serveDocument(document);
        }

        // Kiểm tra xem người dùng có đơn hàng không
        List<Order> orderList = orderService.findByUserIdAndBookId(userId, id); // modify
        if (orderList == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

//        // find activation code with status USED
//        ActivationCode activation = activationCodeService.findValidCodeWithOrderIdAndBookId(orderList, id);
//
//        // if you want to check with another status, you can modify this function in activationCodeService
//        if (activationCode == null) {
//            activationCode = new ActivationCode();
//            activationCode.setCode("");
//        }
//        ActivationCode codeToCheck = activationCodeService.findValidActivationCode(activationCode.getCode(), id);
//        System.out.println("1: " + activationCode + "\n2: " + codeToCheck + "\n3: " + activation + "\n4: " + orderList);
//        if (activation == null || !activation.equals(codeToCheck)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }

        // Tìm mã kích hoạt hợp lệ
        ActivationCode activation = activationCodeService.findValidCodeWithOrderIdAndBookId(orderList, id);
        System.out.println("Activation code found: " + activation);

        if (activation == null || (activationCode != null && !activation.getCode().equals(activationCode.getCode()))) {
            System.out.println("Invalid activation code provided: " + activationCode.getCode());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // check valid date
        if (activation != null && activation.getEndDate().before(new Date())) {
            System.out.println("Activation code " + activation.getCode() + " has expired.");
            activation.setStatus(ActivationCode.ActivationCodeStatus.EXPIRED);
            ActivationCode updatedActivationCode = activationCodeService.updateActivationCode(activation);
            System.out.println("Updated activation code: " + updatedActivationCode);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (page == null) {
            ReadingHistory readingHistory = readingHistoryRepository.findTopByUserIdAndDocumentIdOrderByTimestampDesc(userId, id);
            if (readingHistory != null) {
                page = readingHistory.getCurrentPage();
            } else {
                page = 1; // Giá trị mặc định nếu không tìm thấy lịch sử đọc
            }
        }
        documentService.saveReadingHistory(userId, id, page);
        return serveDocument(document);
    }

    private ResponseEntity<Resource> serveDocument(Document document) {
        Path path = Paths.get(document.getPdfUrl());
        if (!Files.exists(path)) {
            System.out.println("File does not exist at path: " + path);
            // Trả về lỗi 500 nếu không thể tìm thấy tài liệu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // Đọc tài liệu từ đĩa
        ByteArrayResource resource = null;
        if(document.getDrmEnabled()){
            EncodeDocument encodeDocument = edService.findByDocumentId(document.getId());
            resource = openEncryptedPdf(document.getPdfUrl(), AESUtil.decrypt(encodeDocument.getPassword()));
            System.out.println("decrypt: " + encodeDocument.getPassword() + "password: " + AESUtil.decrypt(encodeDocument.getPassword()));
        }else{
            resource = readPdf(document.getPdfUrl());
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    public static ByteArrayResource readPdf(String source) {
        try {
            PDDocument pdDocument = PDDocument.load(new File(source));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pdDocument.save(byteArrayOutputStream);

            pdDocument.close();

            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PostMapping("/reading-history")
    public ResponseEntity<Void> saveReadingHistory(
            @RequestBody ReadingHistory readingHistory
    ) {
        documentService.saveReadingHistory(readingHistory.getUserId(), readingHistory.getDocumentId(), readingHistory.getCurrentPage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reading-history/{userId}")
    public ResponseEntity<List<ReadingHistory>> getReadingHistory(@PathVariable String userId) {
        List<ReadingHistory> readingHistoryList = readingHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
        return ResponseEntity.ok(readingHistoryList);
    }


}