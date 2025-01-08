package com.javaacademy.cryptowallet;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.OperationMoneyBodyDto;
import com.javaacademy.cryptowallet.entity.CoinType;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.service.conversionService.conversionCoinService.ConversionCoinService;
import com.javaacademy.cryptowallet.service.conversionService.conversionRubUsdService.ConversionRubUsdService;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import com.javaacademy.cryptowallet.storage.UserStorage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("local")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    @Value("${app.exchange.standard-usd-cost}")
    private BigDecimal standardUsdPrice;
    @Value("${app.exchange.standard-coin-cost}")
    private BigDecimal standardCoinPrice;
    private final String testLogin = "Alexander";
    private final String testEmail = "Alexander@mail.ru";
    private final String testPassword = "12345";
    private final BigDecimal testAmount = BigDecimal.TEN;
    private final BigDecimal testAmount2 = BigDecimal.ZERO;
    private final String testLogin2 = "Alexander2";
    private final String bitcoinType = "bitcoin";
    private final String failedCoinType = "failcoin";

    @Autowired
    private CryptoAccountStorage cryptoAccountStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ConversionRubUsdService conversionRubUsdService;
    @Autowired
    private ConversionCoinService conversionCoinService;

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
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(testLogin, bitcoinType);
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
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(testLogin, failedCoinType);
        given(requestSpecification)
                .body(cryptoAccountDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Регистрация криптосчета - ошибка, не существующий пользователь")
    public void createCryptoAccountFailedCauseUser() {
        CryptoAccountDto cryptoAccountDto = new CryptoAccountDto(testLogin, bitcoinType);
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
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        cryptoAccountStorage.save(new CryptoAccount(testLogin, CoinType.BTC));
        cryptoAccountStorage.save(new CryptoAccount(testLogin, CoinType.SOL));
        Integer expectedAccounts = 2;
        List<CryptoAccountDto> resultAccounts = given(requestSpecification)
                .param("login", testLogin)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() { });

        assertEquals(expectedAccounts, resultAccounts.size());
    }

    @Test
    @DisplayName("Успешный тест получения всех счетов пользователя - не существующий пользователь")
    public void findAllUserAccountsSuccessNotExistUser() {
        String expectedLogin = "Пользователь не существует";
        int expectedIndex = 0;
        List<CryptoAccountDto> resultAccounts = given(requestSpecification)
                .queryParam("login", testLogin2)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() { });

        assertEquals(expectedLogin, resultAccounts.get(expectedIndex).getLogin());
        }

    @Test
    @DisplayName("Отсутствие счетов у пользователя - успешно")
    public void findAllUserAccountsEmpty() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        String expectedLogin = "Счетов у пользователя %s не обнаружено".formatted(testLogin);
        int expectedIndex = 0;
        List<CryptoAccountDto> resultAccounts = given(requestSpecification)
                .queryParam("login", testLogin)
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() { });

        assertEquals(expectedLogin, resultAccounts.get(expectedIndex).getLogin());
    }

    @Test
    @DisplayName("Успешное пополнение")
    public void topUpSuccess() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount cryptoAccount = new CryptoAccount(testLogin, CoinType.BTC);
        cryptoAccountStorage.save(cryptoAccount);
        OperationMoneyBodyDto operationMoneyBodyDto = new OperationMoneyBodyDto(cryptoAccount.getUuid().toString(),
                testAmount);
        given(requestSpecification)
                .body(operationMoneyBodyDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Пополнение - ошибка 500")
    public void topUpFailed() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount cryptoAccount = new CryptoAccount(testLogin, CoinType.BTC);
        cryptoAccountStorage.save(cryptoAccount);
        OperationMoneyBodyDto operationMoneyBodyDto = new OperationMoneyBodyDto(cryptoAccount.getUuid().toString(),
                testAmount2);
        given(requestSpecification)
                .body(operationMoneyBodyDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Успешное получение баланса в рублях")
    public void getBalanceInRubSuccess() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount cryptoAccount = new CryptoAccount(testLogin, CoinType.BTC);
        cryptoAccountStorage.save(cryptoAccount);
        cryptoAccount.setBalanceCoin(testAmount);
        BigDecimal expected = standardCoinPrice.multiply(testAmount).multiply(standardUsdPrice);
        BigDecimal result = given(requestSpecification)
                .pathParam("id", cryptoAccount.getUuid())
                .get("/balance/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(BigDecimal.class);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Провальное получение баланса - нет счёта")
    public void getBalanceInRubFailed() {
        UUID testUuid = UUID.randomUUID();
        given(requestSpecification)
                .pathParam("id", testUuid)
                .get("/balance/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Успешное получение баланса всех счетов пользователя")
    public void getAllBalanceSuccess() {
        int expectedCountAccounts = 2;
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount bitcoinAccount = new CryptoAccount(testLogin, CoinType.BTC);
        CryptoAccount solanaAccount = new CryptoAccount(testLogin, CoinType.SOL);
        bitcoinAccount.setBalanceCoin(testAmount);
        solanaAccount.setBalanceCoin(testAmount);
        cryptoAccountStorage.save(bitcoinAccount);
        cryptoAccountStorage.save(solanaAccount);

        BigDecimal expected = standardCoinPrice.multiply(testAmount)
                .multiply(standardUsdPrice).multiply(BigDecimal.valueOf(expectedCountAccounts));
        BigDecimal result = given(requestSpecification)
                .queryParam("login", testLogin)
                .get("/balance")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(BigDecimal.class);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Провальное получение баланса всех счетов пользователя, отсутствуют счета")
    public void getAllBalanceFailed() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));

        given(requestSpecification)
                .queryParam("login", testLogin)
                .get("/balance")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Провальное получение баланса всех счетов пользователя, несуществующий пользователь")
    public void getAllBalanceFailedCauseUser() {
        given(requestSpecification)
                .queryParam("login", testLogin)
                .get("/balance")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("Успешное снятие денег")
    public void withdrawalSuccess() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount cryptoAccount = new CryptoAccount(testLogin, CoinType.BTC);
        cryptoAccountStorage.save(cryptoAccount);
        cryptoAccount.setBalanceCoin(BigDecimal.ONE);
        OperationMoneyBodyDto operationMoneyBodyDto = new OperationMoneyBodyDto(cryptoAccount.getUuid().toString(),
                testAmount);
        BigDecimal countWithdrawalCoin = operationMoneyBodyDto.getAmountRubles()
                .divide(standardUsdPrice).divide(standardCoinPrice);
        String result = given(requestSpecification)
                .body(operationMoneyBodyDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asPrettyString();
        String expected = "Операция прошла успешно. Продано %s %s."
                .formatted(countWithdrawalCoin, cryptoAccount.getCoinType());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Провальное снятие денег - отрицательная сумма")
    public void withdrawalFailed() {
        BigDecimal negativeAmount = BigDecimal.ONE.negate();
        OperationMoneyBodyDto operationMoneyBodyDto = new OperationMoneyBodyDto(UUID.randomUUID().toString(),
                negativeAmount);
        String result = given(requestSpecification)
                .body(operationMoneyBodyDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asPrettyString();
        String expected = "Нельзя ввести отрицательную сумму";
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Провальное снятие денег - недостаточно средств")
    public void withdrawalFailedNotEnoughMoney() {
        userStorage.saveUser(new User(testLogin, testEmail, testPassword));
        CryptoAccount cryptoAccount = new CryptoAccount(testLogin, CoinType.BTC);
        cryptoAccountStorage.save(cryptoAccount);
        cryptoAccount.setBalanceCoin(BigDecimal.ZERO);
        OperationMoneyBodyDto operationMoneyBodyDto = new OperationMoneyBodyDto(cryptoAccount.getUuid().toString(),
                testAmount);
        BigDecimal countWithdrawalCoin = operationMoneyBodyDto.getAmountRubles()
                .divide(standardUsdPrice).divide(standardCoinPrice);
        String result = given(requestSpecification)
                .body(operationMoneyBodyDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asPrettyString();
        String expected = "Операция отклонена, на счёте недостаточно средств";
        assertEquals(expected, result);
    }
}
