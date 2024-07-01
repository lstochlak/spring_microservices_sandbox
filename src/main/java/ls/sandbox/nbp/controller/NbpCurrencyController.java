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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.service.NbpMiddleExchangeRateService;
import ls.sandbox.nbp.service.NbpSellExchangeRateService;
import ls.sandbox.nbp.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
     * @param date date in RRRR-MM-DD (ISO 8601) format.
     * @param code currency code according to ISO 4217 standard
     * @return sell exchange rate.
     */
    @GetMapping("/nbpSell/{code}/{date}")
    public Double getSellExchangeRate(@PathVariable("code") String code, @PathVariable("date") String date)
    {
        Double result = null;

        try
        {
            result = nbpSellExchangeRateService.getSellRate(code, CommonUtils.parseDateFromString(date));
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              MessageFormat.format("Unexpected error! {0}", e.getMessage()),
                                              e);
        }

        if (null == result)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                              MessageFormat.format("Exchange rate not found for code={0} date={1} !",
                                                                   code, date));
        }

        return result;
    }

    /**
     * Calculates the total purchasing cost in PLN of given list of foreign currencies for given date.
     *
     * @param date          date in RRRR-MM-DD (ISO 8601) format.
     * @param currencyCodes list of currency codes according to ISO 4217 standard.
     * @return total purchasing cost.
     */
    @GetMapping("/nbpPurchase/{date}")
    public Double getPurchaseCost(@PathVariable("date") String date, @PathParam(value = "currencyCodes") String currencyCodes)
    {
        Double result = null;

        try
        {
            JSONArray jsonArray = new JSONArray(currencyCodes);
            List<String> codes = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i++) {
                codes.add(jsonArray.getString(i));
            }

            result = nbpMiddleExchangeRateService.getPurchaseCost(CommonUtils.parseDateFromString(date), codes);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              MessageFormat.format("Unexpected error! {0}", e.getMessage()),
                                              e);
        }

        if (null == result)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MessageFormat.format(
                    "Purchase cost can''t be evaluated for codes={0} date={1} !", currencyCodes, date));
        }

        return result;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value not found!")
    @ExceptionHandler({ EntityNotFoundException.class })
    public void entityNotFoundExceptionHandler()
    {
        log.debug("Value not found!");
    }

}
//------------------------------------------------------------------------------
