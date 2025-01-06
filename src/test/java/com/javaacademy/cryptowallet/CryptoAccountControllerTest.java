package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CoinType;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exception.UserNotExistException;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
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

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("local")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    private final String testLogin = "Alexander";
    private final String testLogin2 = "Alexander2";
    private final String testCoinType = "bitcoin";
    private final String failedCoinType = "failcoin";

    @Autowired
    private CryptoAccountService cryptoAccountService;
    @Autowired
    private CryptoAccountStorage cryptoAccountStorage;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/cryptowallet")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Успешная регистрация криптосчета")
    public void createCryptoAccountSuccess() {
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(testLogin, testCoinType);
        given(requestSpecification)
                .body(cryptoAccountDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Регистрация криптосчета - ошибка")
    public void createCryptoAccountFailed() {
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(testLogin, failedCoinType);
        given(requestSpecification)
                .body(cryptoAccountDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Успешное получение всех счетов пользователя")
    public void findAllUserAccountsSuccess() {
        cryptoAccountStorage.save(new CryptoAccount(testLogin, CoinType.BTC));
        cryptoAccountStorage.save(new CryptoAccount(testLogin, CoinType.SOL));
        Integer expectedAccounts = 2;
        List<CryptoAccountDto> resultAccounts = given(requestSpecification)
                .param(testLogin)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {});

        Assertions.assertEquals(expectedAccounts, resultAccounts.size());
    }

    @Test
    @DisplayName("Провальный тест получения всех счетов пользователя - не существующий пользователь")
    public void findAllUserAccountsFailed() {
        String expectedLogin = "Пользователя с таким логином не существует";
        String expectedCoinType = "";
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(expectedLogin, expectedCoinType);
        List<CryptoAccountDto> expectedAccounts = new ArrayList<>(List.of(cryptoAccountDto));

        List<CryptoAccountDto> resultAccounts = given(requestSpecification)
                .param(testLogin2)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {});

        Assertions.assertEquals(expectedAccounts, resultAccounts);
    }
}
