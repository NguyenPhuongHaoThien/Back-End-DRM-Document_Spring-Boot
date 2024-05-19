package tdtu.edu.vn.service.ebook;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.ActivationCode;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.repository.ActivationCodeRespository;
import tdtu.edu.vn.repository.OrderRepository;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActivationCodeService {
    private ActivationCodeRespository activationCodeRepository;
    private OrderRepository orderRepository;


    public ActivationCode findValidActivationCode(String code, String bookId) {
        return activationCodeRepository.findByCodeAndStatusAndBookWithDrmIdsContains(code, ActivationCode.ActivationCodeStatus.USED, bookId);
    }

    public ActivationCode createActivationCode(Order order, List<String> drm_documents, int validDays) {
        ActivationCode activationCode =
                new ActivationCode(null,
                        order.getId(),
                        generateUniqueCode(),
                        drm_documents,
                        new Date(),
                        new Date(System.currentTimeMillis() + validDays  * 60 * 1000L), // set valid minutes (change after)
                        ActivationCode.ActivationCodeStatus.UNUSED
                );

        activationCodeRepository.save(activationCode);

        order.setActivationCodeId(activationCode.getId());
        orderRepository.save(order);
        System.out.println("New activation code created: " + activationCode);
        return activationCode;
    }

    public String generateUniqueCode() {
        Random random = new Random();
        int length = 16;

        // with 10 digits and 26 characters (use ascii code to convert to character)
        return System.currentTimeMillis() + random.ints(length, 0, 36)
                .mapToObj(i -> i < 10 ? String.valueOf(i) : String.valueOf((char) (i + 55)))
                .collect(Collectors.joining());
    }

    public ActivationCode findValidActivationCodeForDocument(String bookId) {
        return activationCodeRepository.findByStatusAndBookWithDrmIdsContainsAndEndDateAfter(ActivationCode.ActivationCodeStatus.USED, bookId, new Date());
    }


    public ActivationCode findActivationCodeIdByOrderId(String orderId) {
        return activationCodeRepository.findByOrderId(orderId);
    }

    public ActivationCode findValidCodeWithOrderIdAndBookId(List<Order> orders, String bookId) {
        Date currentDate = new Date();
        System.out.println("Searching for valid activation code with orders: " + orders + ", bookId: " + bookId);

        ActivationCode validCode = activationCodeRepository.findAll().stream()
                .filter(activationCode -> {
                    boolean match = orders.stream().anyMatch(order -> order.getId().equals(activationCode.getOrderId()));
                    System.out.println("Activation code " + activationCode.getCode() + " matches order: " + match);
                    return match;
                })
                .filter(activationCode -> {
                    boolean used = activationCode.getStatus().equals(ActivationCode.ActivationCodeStatus.USED);
                    System.out.println("Activation code " + activationCode.getCode() + " is used: " + used);
                    return used;
                })
                .filter(activationCode -> {
                    boolean notExpired = !activationCode.getStatus().equals(ActivationCode.ActivationCodeStatus.EXPIRED);
                    System.out.println("Activation code " + activationCode.getCode() + " is not expired: " + notExpired);
                    return notExpired;
                })
                .filter(activationCode -> {
                    boolean containsBookId = activationCode.getBookWithDrmIds().contains(bookId);
                    System.out.println("Activation code " + activationCode.getCode() + " contains bookId " + bookId + ": " + containsBookId);
                    return containsBookId;
                })
                .filter(activationCode -> {
                    boolean validDate = activationCode.getEndDate().after(currentDate);
                    System.out.println("Activation code " + activationCode.getCode() + " is valid date: " + validDate);
                    if (!validDate) {
                        activationCode.setStatus(ActivationCode.ActivationCodeStatus.EXPIRED);
                        updateActivationCode(activationCode);
                    }
                    return validDate;
                })
                .findFirst()
                .orElse(null);

        System.out.println("Valid activation code found: " + validCode);
        return validCode;
    }

    public ActivationCode getActivationCodeById(String id) {
        return activationCodeRepository.findById(id).orElse(null);
    }

    public ActivationCode updateActivationCode(ActivationCode activationCode) {
        if (activationCodeRepository.existsById(activationCode.getId())) {
            ActivationCode updatedActivationCode = activationCodeRepository.save(activationCode);
            System.out.println("Activation code updated: " + updatedActivationCode);
            return updatedActivationCode;
        }
        System.out.println("Activation code not found: " + activationCode.getId());
        return null;
    }

    public void deleteActivationCode(String id) {
        activationCodeRepository.deleteById(id);
    }
}