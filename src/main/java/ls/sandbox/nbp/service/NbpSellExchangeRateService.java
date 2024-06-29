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
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.NbpSellExchangeRateDto;
import ls.sandbox.nbp.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private LocalCacheService localCacheService;

    @Autowired
    private NbpRemoteService nbpRemoteService;

    /**
     * Returns NBP sell exchange rate for selected currency and date.
     * <br>
     * First checks locally if can't find calls NBP service and writes results locally.
     *
     * @param date date in RRRR-MM-DD (ISO 8601) format
     * @param code currency code according to ISO 4217 standard
     * @return sell exchange rate
     */
    public Double getSellRate(String code, Date date)
    {
        Double result = null;
        String dateAsString = CommonUtils.toStringFromDate(date);

        try
        {
            NbpSellExchangeRateDto localRateDto = localCacheService.findSellExchangeRate(code, date);

            if (null != localRateDto)
            {
                result = localRateDto.getRate();

                log.debug("Sell exchange rate for code={} and date={} found in local DB! -> {}", code, dateAsString, result);
            }
            else
            {
                log.info("Sell exchange rate for code={} and date={} not found in local DB.", code, dateAsString);

                //call NBP service
                NbpSellExchangeRateDto rateDto = nbpRemoteService.getSellExchangeRateFromNbpService(code, date);

                if (null != rateDto)
                {
                    localCacheService.cacheSellExchangeRate(rateDto);

                    result = rateDto.getRate();
                }

            }
        }
        catch (Exception e)
        {
            log.error(CommonUtils.UNEXPECTED_EXCEPTION, e.getMessage(), e);

            throw e;
        }

        return result;
    }
}
//------------------------------------------------------------------------------
