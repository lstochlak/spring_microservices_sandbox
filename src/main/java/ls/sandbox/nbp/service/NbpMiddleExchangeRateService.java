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
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.NbpMiddleExchangeRateDto;
import ls.sandbox.nbp.util.CommonUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

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
    private LocalCacheService localCacheService;

    @Autowired
    private NbpRemoteService nbpRemoteService;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    /**
     * Returns total purchasing cost in PLN of given list of foreign currencies for given date.
     * <br>
     * First checks locally if can't find calls NBP service and writes results locally.
     *
     * @param date  date in RRRR-MM-DD (ISO 8601) format
     * @param codes list of currency codes according to ISO 4217 standard
     * @return total cost
     */
    public Double getPurchaseCost(Date date, List<String> codes)
    {
        final Double[] result = { 0.0 };
        final String dateAsString = CommonUtils.toStringFromDate(date);

        codes.forEach(code -> evaluatePurchaseCost(date, code, result, dateAsString));

        return result[0];
    }

    private void evaluatePurchaseCost(Date date, String code, Double[] result, String dateAsString)
    {
        try
        {
            NbpMiddleExchangeRateDto localRateDto = localCacheService.findMiddleExchangeRate(code, date);

            if (null != localRateDto)
            {
                result[0] += localRateDto.getRate();

                log.debug("Sell exchange rate for code={} and date={} found in local DB! -> {}", code, dateAsString,
                          result);
            }
            else
            {
                log.log(Level.INFO, "Sell exchange rate for code=" + code + " and date=" + dateAsString
                                    + " not found in local DB.");

                //call NBP service
                NbpMiddleExchangeRateDto rateDto = nbpRemoteService.getMiddleExchangeRateFromNbpService(code, date);

                if (null != rateDto)
                {

                    result[0] += rateDto.getRate(); //@TODO add value check (?)
                }
            }
        }
        catch (Exception e)
        {
            log.log(Level.ERROR, "Unexpected exception! " + e.getMessage(), e);

            throw e;
        }
    }
}
//------------------------------------------------------------------------------
