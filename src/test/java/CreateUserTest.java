import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;


public class CreateUserTest {
    private String email;
    private String password;
    private String name;
    User user;
    UserApi userApi = new UserApi();
    @Before
    @DisplayName("Генерация данных пользователя пользователя")
    public void userGeneration(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        user = new User(name, email, password);
    }
    @Test
    @DisplayName("Создание пользователя")
    public void userCreate(){
        userApi
                .createNewUser(user)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
    @Test
    @DisplayName("Создание пользователя повторно")
    public void userCreateDouble(){
        userApi
                .createNewUser(user);
        userApi
                .createNewUser(user)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", is("User already exists"));
    }
    @Test
    @DisplayName("Создание пользователя c пропуском обязательного поля")
    public void userCreateNotField(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = "";
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        User notPasswordUser = new User(name, email, password);
        userApi
                .createNewUser(notPasswordUser)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", is("Email, password and name are required fields"));
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
