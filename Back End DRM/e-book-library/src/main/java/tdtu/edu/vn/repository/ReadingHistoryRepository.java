package tdtu.edu.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import tdtu.edu.vn.model.ReadingHistory;

import java.util.List;

public interface ReadingHistoryRepository extends MongoRepository<ReadingHistory, String> {
    ReadingHistory findTopByUserIdAndDocumentIdOrderByTimestampDesc(String userId, String documentId);
    Page<ReadingHistory> findByUserId(String userId, Pageable pageable);


    List<ReadingHistory> findByUserIdOrderByTimestampDesc(String userId);


}
