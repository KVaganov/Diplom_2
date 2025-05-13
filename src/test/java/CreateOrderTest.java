import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
public class CreateOrderTest {
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    private String email;
    private String password;
    private String name;
    String response;

    User user;
    @Before
    @DisplayName("Генерация данных пользователя пользователя")
    public void userGeneration(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        user = new User(name, email, password);
        userApi
                .createNewUser(user);
    }
    @Test
    @DisplayName("Создание заказа")
    public void orderCreate() {
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        List<String> listIngredient = orderApi
                .getIngredientsList()
                .extract()
                .body()
                .path("data._id");
        orderApi
                .createOrder(response, listIngredient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа под неавторизованным пользователем")
    public void orderCreateNotLoginUser() {
        List<String> listIngredient = orderApi
                .getIngredientsList()
                .extract()
                .body()
                .path("data._id");
        orderApi
                .createOrder("response", listIngredient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа c невалидным хешем")
    public void orderCreateNotValid() {
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        List<String> listIngredient = List.of("orderIngridient", "orderIngridient");
        orderApi
                .createOrder(response, listIngredient)
                .statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа без ингридиентов")
    public void orderCreateNotIngredient() {
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        List<String> listIngredient = List.of();
        orderApi
                .createOrder(response, listIngredient)
                .statusCode(400)
                .body("message", is("Ingredient ids must be provided"));
    }
    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash() {
        String response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        if (response != null) {
            userApi.deleteUser(response);
        }
    }
}
