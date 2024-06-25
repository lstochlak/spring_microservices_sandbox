//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp
//
// File Name : NbpCurrencySpringBootApp.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main spring boot class for currency micro-service.
 *
 * @author Lukasz.Stochlak
 */
@SpringBootApplication
@Log4j2
public class NbpCurrencySpringBootApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(NbpCurrencySpringBootApp.class, args);
    }
}
//------------------------------------------------------------------------------
