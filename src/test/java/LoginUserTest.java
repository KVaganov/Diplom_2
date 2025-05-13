import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class LoginUserTest {
    private String email;
    private String password;
    private String name;
    User user;
    UserApi userApi = new UserApi();
    @Before
    @DisplayName("Создание пользователя")
    public void userNewCreate(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        user = new User(name, email, password);
        userApi
                .createNewUser(user);
    }

    @Test
    @DisplayName("Логин пользователя")
    public void userLogin(){
        userApi
                .loginUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));
    }
    @Test
    @DisplayName("Логин не созданного пользователя")
    public void userLoginNotCreate(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        User noAutorizedUser = new User(name, email, password);
        userApi
                .loginUser(noAutorizedUser)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
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
