package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.repository.UserRepository;
import com.javaacademy.cryptowallet.service.UserService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("local")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/cryptowallet/user")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .setDefaultParser(Parser.JSON)
            .log(LogDetail.ALL)
            .build();
    private final UserDto userDto = UserDto.builder()
            .login("Alexander")
            .email("alexander@mail.ru")
            .password("12345")
            .build();

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void saveUserSuccess() {
        given(requestSpecification)
                .body(userDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(201);
    }

    @Test
    @DisplayName("Успешная смена пароля")
    public void resetPasswordSuccess() {
        userService.saveUser(userDto);
        String Login = "Alexander";
        String email = "alexander@mail.ru";
        String password = "54321";
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto("Alexander", "12345", "54321");
        User resultUser = userRepository.getUserByLogin(resetPasswordDto.getLogin());

        given(requestSpecification)
                .body(resetPasswordDto)
                .patch("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.ACCEPTED.value());

        assertEquals(Login, resultUser.getLogin());
        assertEquals(email, resultUser.getEmail());
        assertEquals(password, resultUser.getPassword());
    }

    @Test
    @DisplayName("Регистрация пользователя - ошибка")
    public void saveUserFailed() {
        userService.saveUser(userDto);
        given(requestSpecification)
                .body(userDto)
                .post("/signup")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Смена пароля - ошибка")
    public void resetPasswordFailed() {
        userService.saveUser(userDto);
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto("Alexander", "123456", "54321");
        given(requestSpecification)
                .body(resetPasswordDto)
                .patch("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(500);
    }
}
