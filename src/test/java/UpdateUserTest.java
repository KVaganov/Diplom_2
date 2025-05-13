import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class UpdateUserTest {
    private String email;
    private String password;
    private String name;
    String response;
    User user;
    UserApi userApi = new UserApi();
    @Before
    @DisplayName("Создание пользователя")
    public void userCreate(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        user = new User(name, email, password);
        userApi
                .createNewUser(user);
    }
    @Test
    @DisplayName("Изменение данных пользователя")
    public void userPatch(){
        response = userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(9) + "@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(9));
        user.setName(RandomStringUtils.randomAlphabetic(9));
        userApi.updateUser(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));
    }
    @Test
    @DisplayName("Изменение данных несозданного пользователя")
    public void userPatchNotCreate(){
        userApi
                .loginUser(user)
                .extract().body()
                .path("accessToken");
        user.setEmail(RandomStringUtils.randomAlphabetic(9) + "@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(9));
        user.setName(RandomStringUtils.randomAlphabetic(9));
        userApi.updateUser("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
