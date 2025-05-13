import lombok.Data;
import java.util.List;
@Data
public class Order {
    @lombok.Getter
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

