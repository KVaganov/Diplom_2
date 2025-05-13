import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String name;
    private String email;
    private String password;
   public User (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
