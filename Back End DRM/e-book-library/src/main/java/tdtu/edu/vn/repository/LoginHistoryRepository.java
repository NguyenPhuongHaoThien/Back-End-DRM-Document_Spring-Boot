package tdtu.edu.vn.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tdtu.edu.vn.model.LoginHistory;

import java.util.List;

public interface LoginHistoryRepository extends MongoRepository<LoginHistory, String> {
    LoginHistory findByUserId(String userId);
}
