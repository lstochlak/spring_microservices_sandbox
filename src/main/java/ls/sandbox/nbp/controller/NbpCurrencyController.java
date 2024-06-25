//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.controller
//
// File Name : NbpCurrencyController.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.controller;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.service.NbpMiddleExchangeRateService;
import ls.sandbox.nbp.service.NbpSellExchangeRateService;
import ls.sandbox.nbp.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for currency micro-service.
 *
 * @author Lukasz.Stochlak
 */
@RestController
@RequestMapping("/api/currency")
@Log4j2
public class NbpCurrencyController
{
    @Autowired
    private NbpMiddleExchangeRateService nbpMiddleExchangeRateService;

    @Autowired
    private NbpSellExchangeRateService nbpSellExchangeRateService;

    /**
     * Returns NBP sell exchange rate.
     *
     * @param date date in RRRR-MM-DD (ISO 8601) format
     * @param code currency code according to ISO 4217 standard
     * @return sell exchange rate
     */
    @GetMapping("/nbpSell/{code}/{date}")
    public Double getSellExchangeRate(@PathVariable("code") String code, @PathVariable("date") String date)
    {
        return nbpSellExchangeRateService.getSellRate(code, CommonUtils.parseDateFromString(date));
    }

    /**
     * Calculates the total purchasing cost in PLN of given list of foreign currencies for given date.
     *
     * @param date          date in RRRR-MM-DD (ISO 8601) format
     * @param currencyCodes list of currency codes according to ISO 4217 standard
     * @return total purchasing cost.
     */
    @GetMapping("/nbpPurchase/{date}")
    public Double getPurchaseCost(@PathVariable("date") String date, @RequestBody List<String> currencyCodes)
    {
        return nbpMiddleExchangeRateService.getPurchaseCost(CommonUtils.parseDateFromString(date), currencyCodes);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value not found!")
    @ExceptionHandler({ EntityNotFoundException.class })
    public void entityNotFoundExceptionHandler()
    {
        log.debug("Value not found!");
    }

}
//------------------------------------------------------------------------------
