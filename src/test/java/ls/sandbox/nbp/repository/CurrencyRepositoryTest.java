//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.repository
//
// File Name : CurrencyRepositoryTest.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 26.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.repository;

import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.NbpCurrencySpringBootApp;
import ls.sandbox.nbp.model.Currency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Lukasz.Stochlak
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NbpCurrencySpringBootApp.class)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class CurrencyRepositoryTest
{
    @Autowired
    private CurrencyRepository repository;

    @Test
    void getAllCurrencies()
    {
        List<Currency> result = repository.findAll();

        Assertions.assertNotNull(result);

        log.info("Result: {}", result);
    }
}
//------------------------------------------------------------------------------
