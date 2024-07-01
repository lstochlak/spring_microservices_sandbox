//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp
//
// File Name : NbpCurrencySpringBootAppTest.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 27.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.util.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Lukasz.Stochlak
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NbpCurrencySpringBootApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class NbpCurrencySpringBootAppTest
{
    private final static int DEFINED_PORT = 8090;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServerProperties serverProperties;

    @Test
    @Order(1)
    void servicePortTest()
    {
        int port = serverProperties.getPort();

        Assertions.assertEquals(DEFINED_PORT, port);
    }

    @Test
    @Order(2)
    void nbpSellServicesTest()
    {
        String date = CommonUtils.toStringFromDate(new Date());

        String code = "USD";

        Double response = null;

        ResponseEntity<Double> responseEntity =
                this.restTemplate.getForEntity("http://localhost:8090/api/currency/nbpSell/{code}/{date}", Double.class,
                                               code, date);

        Assertions.assertNotNull(responseEntity);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK))
        {
            response = responseEntity.getBody();

            Assertions.assertNotNull(response);
        }
        else
        {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        log.info("Result: {}", response);
    }

    @Test
    @Order(3)
    void nbpPurchaseServicesTest()
    {
        String date = CommonUtils.toStringFromDate(new Date());

        HttpEntity<List<String>> request = new HttpEntity<>(Arrays.asList("USD", "AUD", "CAD"));

        Double response = null;

        ResponseEntity<Double> responseEntity =
                this.restTemplate.postForEntity("http://localhost:8090/api/currency/nbpPurchase/{date}", request,
                                                Double.class, date);

        Assertions.assertNotNull(responseEntity);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK))
        {
            response = responseEntity.getBody();

            Assertions.assertNotNull(response);
        }
        else
        {

            Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        log.info("Result: {}", response);
    }
}
//------------------------------------------------------------------------------
