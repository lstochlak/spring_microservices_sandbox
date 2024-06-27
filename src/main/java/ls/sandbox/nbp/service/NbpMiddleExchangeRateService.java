//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.service
//
// File Name : NbpMiddleExchangeRateService.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.service;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.TableData;
import ls.sandbox.nbp.model.Currency;
import ls.sandbox.nbp.model.NbpMiddleExchangeRate;
import ls.sandbox.nbp.repository.CurrencyRepository;
import ls.sandbox.nbp.repository.NbpMiddleExchangeRateRepository;
import ls.sandbox.nbp.util.CommonUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for NBP middle exchange rate.
 *
 * @author Lukasz.Stochlak
 */
@Service
@Log4j2
public class NbpMiddleExchangeRateService
{
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private NbpMiddleExchangeRateRepository nbpMiddleExchangeRateRepository;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    /**
     * Returns total purchasing cost in PLN of given list of foreign currencies for given date.
     * <br>
     * First checks locally if can't find calls NBP service and writes results locally.
     *
     * @param date date in RRRR-MM-DD (ISO 8601) format
     * @param codes list of currency codes according to ISO 4217 standard
     * @return total cost
     */
    public Double getPurchaseCost(Date date, List<String> codes)
    {
        final Double[] result = { 0.0 };
        final String dateAsString = CommonUtils.toStringFromDate(date);

        codes.forEach(code ->
                      {
                          try
                          {
                              NbpMiddleExchangeRate nbpMiddleExchangeRate =
                                      nbpMiddleExchangeRateRepository.findByCodeAndDate(code, date);

                              result[0] += nbpMiddleExchangeRate.getRate();
                          }
                          catch (EntityNotFoundException | NullPointerException e)
                          {
                              log.log(Level.INFO, "Exchange rate for code=" + code + " and date=" + dateAsString
                                                  + " not found in local DB.");

                              //call NBP service
                              TableData nbpTableData = getRateFromNbpService(code, dateAsString);

                              result[0] += Double.parseDouble(nbpTableData.getRates().get(0).getMid());
                          }
                          catch (Exception e)
                          {
                              log.log(Level.ERROR, "Unexpected exception! " + e.getMessage(), e);

                              throw e;
                          }

                      });

        return result[0];
    }

    private TableData getRateFromNbpService(String code, String dateAsString)
    {
        RestTemplate restTemplate = restTemplateBuilder.build();

        TableData nbpTableData =
                restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/A/{code}/{date}",
                                          TableData.class, code, dateAsString);

        cacheRate(nbpTableData);
        return nbpTableData;
    }

    private void cacheRate(TableData nbpTableData)
    {
        Currency currency = new Currency();
        currency.setCode(nbpTableData.getCode());
        currency.setCurrency(nbpTableData.getCurrency());

        NbpMiddleExchangeRate nbpMiddleExchangeRate = new NbpMiddleExchangeRate();
        nbpMiddleExchangeRate.setCurrency(currency);
        nbpMiddleExchangeRate.setDate(
                CommonUtils.parseDateFromString(nbpTableData.getRates().get(0).getEffectiveDate()));
        nbpMiddleExchangeRate.setRate(Double.parseDouble(nbpTableData.getRates().get(0).getMid()));

        currencyRepository.saveAndFlush(currency);
        nbpMiddleExchangeRateRepository.saveAndFlush(nbpMiddleExchangeRate);
    }

}
//------------------------------------------------------------------------------
