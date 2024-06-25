//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.service
//
// File Name : NbpSellExchangeRateService.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.service;

import java.util.Date;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.NbpTableData;
import ls.sandbox.nbp.model.Currency;
import ls.sandbox.nbp.model.NbpSellExchangeRate;
import ls.sandbox.nbp.repository.CurrencyRepository;
import ls.sandbox.nbp.repository.NbpSellExchangeRateRepository;
import ls.sandbox.nbp.util.CommonUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for NBP sell exchange rate.
 *
 * @author Lukasz.Stochlak
 */
@Service
@Log4j2
public class NbpSellExchangeRateService
{
    @Autowired
    private NbpSellExchangeRateRepository nbpSellExchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    /**
     * Returns NBP sell exchange rate for selected currency and date.
     * <br>
     * First checks locally if can't find calls NBP service and writes results locally.
     *
     * @param date date in RRRR-MM-DD (ISO 8601) format
     * @param code currency code according to ISO 4217 standard
     * @return sell exchange rate
     */
    public double getSellRate(String code, Date date)
    {
        double result = -1.0;

        try
        {
            result = nbpSellExchangeRateRepository.findByCodeAndDate(code, date).getRate();
        }
        catch (EntityNotFoundException | NullPointerException e)
        {
            String dateAsString = CommonUtils.toStringFromDate(date);

            log.log(Level.INFO,
                    "Exchange rate for code=" + code + " and date=" + dateAsString + " not found in local DB.");

            //call NBP service
            NbpTableData nbpTableData = getRateFromNbpService(code, dateAsString);

            result = Double.parseDouble(nbpTableData.getRates().get(0).getAsk());
        }
        catch (Exception e)
        {
            log.log(Level.ERROR, "Unexpected exception! " + e.getMessage(), e);

            throw e;
        }

        return result;
    }

    private NbpTableData getRateFromNbpService(String code, String dateAsString)
    {
        RestTemplate restTemplate = restTemplateBuilder.build();

        NbpTableData nbpTableData =
                restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/{code}/{date}",
                                          NbpTableData.class, code, dateAsString);

        cacheRate(nbpTableData);

        return nbpTableData;
    }

    private void cacheRate(NbpTableData nbpTableData)
    {
        Currency currency = new Currency();
        currency.setCode(nbpTableData.getCode());
        currency.setCurrency(nbpTableData.getCurrency());

        NbpSellExchangeRate nbpSellExchangeRate = new NbpSellExchangeRate();
        nbpSellExchangeRate.setCurrency(currency);
        nbpSellExchangeRate.setDate(CommonUtils.parseDateFromString(nbpTableData.getRates().get(0).getEffectiveDate()));
        nbpSellExchangeRate.setRate(Double.parseDouble(nbpTableData.getRates().get(0).getAsk()));

        currencyRepository.saveAndFlush(currency);
        nbpSellExchangeRateRepository.saveAndFlush(nbpSellExchangeRate);
    }

}
//------------------------------------------------------------------------------
