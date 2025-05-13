import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class UserApi extends StellarBurger {
    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String PATCH_AND_DELETE_USER = "/api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createNewUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Обновление данных пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .patch(PATCH_AND_DELETE_USER)
                .then();
    }
    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(PATCH_AND_DELETE_USER)
                .then();
    }

}
