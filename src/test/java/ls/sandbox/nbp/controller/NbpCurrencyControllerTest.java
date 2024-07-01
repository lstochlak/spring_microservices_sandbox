//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.controller
//
// File Name : NbpCurrencyControllerTest.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 26.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.StreamSupport;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.NbpCurrencySpringBootApp;
import ls.sandbox.nbp.util.CommonUtils;
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
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Lukasz.Stochlak
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NbpCurrencySpringBootApp.class)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class NbpCurrencyControllerTest
{
    @Autowired
    private NbpCurrencyController controller;

    @Test
    void getSellExchangeRate()
    {
        final String code = "USD";
        final String date = CommonUtils.toStringFromDate(new Date());;

        Double result = controller.getSellExchangeRate(code, date);

        Assertions.assertNotNull(result);

        log.debug("Result: {}", result);
    }

    @Test
    void getPurchaseCost()
    {
        final String codes = "[\"USD\",\"AUD\",\"CAD\"]";

        final String date = CommonUtils.toStringFromDate(new Date());

        Double result = controller.getPurchaseCost(date, codes);

        Assertions.assertNotNull(result);

        log.debug("Result: {}", result);
    }
}
//------------------------------------------------------------------------------
