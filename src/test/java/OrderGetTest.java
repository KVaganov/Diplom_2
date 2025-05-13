import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
public class OrderGetTest {
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
    @DisplayName("Получение заказа")
    public void orderCreate() {
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        orderApi
                .getUsersOrder(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());

    }
    @Test
    @DisplayName("Получение заказа неавторизированного пользователя")
    public void orderCreateNotUserLogin() {
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        orderApi
                .getUsersOrder("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));

    }
    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash(){
        String response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        if (response != null){
            userApi.deleteUser(response);
        }
    }
}
