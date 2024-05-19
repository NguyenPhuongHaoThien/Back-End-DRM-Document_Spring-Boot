package tdtu.edu.vn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "LoginHistory")
public class LoginHistory implements Serializable {
    @Id
    private String userId;
    private List<String> ipAddresses = new ArrayList<>();
    private Date lastLoginTime;
    private String userAgent;
    private String location;

}
