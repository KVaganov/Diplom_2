import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import static io.restassured.RestAssured.given;

public class OrderApi extends StellarBurger{
    private static final String ORDER_LIST = "/api/orders";
    private static final String INGREDIENTS_LIST = "/api/ingredients/";

    @Step("Получение данных ингридиентов")
    public ValidatableResponse getIngredientsList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_LIST)
                .then();
    }

    @Step("Создание заказа юзером")
    public ValidatableResponse createOrder(String accessToken, List<String> ingredients) {
        Order requestBody = new Order(ingredients);
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(ORDER_LIST)
                .then();
    }

    @Step("Получение списка заказов пользователя")
    public ValidatableResponse getUsersOrder(String accessToken, User user){
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_LIST)
                .then();
    }

}


