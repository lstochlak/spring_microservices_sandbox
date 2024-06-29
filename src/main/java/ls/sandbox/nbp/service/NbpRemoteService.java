//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.service
//
// File Name : NbpRemoteService.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 28.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.CurrencyDto;
import ls.sandbox.nbp.dto.ExchangeRateDto;
import ls.sandbox.nbp.dto.NbpMiddleExchangeRateDto;
import ls.sandbox.nbp.dto.NbpSellExchangeRateDto;
import ls.sandbox.nbp.dto.TableData;
import ls.sandbox.nbp.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lukasz.Stochlak
 */
@Service
@Log4j2
public class NbpRemoteService
{

    protected static final String CALLING_REMOTE_NBP_SERVICE = "Calling remote NBP service...";

    protected static final String RETRIEVED_NBP_SERVICE_FOR_CODE_AND_DATE =
            "Successfully retrieved NBP service for code={} and date={} -> {}";

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    /**
     * Calls NBP remote service for sell exchange rate for selected currency on a given day.
     *
     * @param code currency code according to ISO 4217 standard
     * @param date date
     * @return DTO for {@link ls.sandbox.nbp.model.NbpSellExchangeRate}
     */
    public NbpSellExchangeRateDto getSellExchangeRateFromNbpService(String code, Date date)
    {
        String dateAsString = CommonUtils.toStringFromDate(date);
        RestTemplate restTemplate = restTemplateBuilder.build();

        TableData nbpTableData = null;
        NbpSellExchangeRateDto result = null;

        log.info(CALLING_REMOTE_NBP_SERVICE);

        try
        {
            nbpTableData = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/{code}/{date}",
                                                     TableData.class, code, dateAsString);

            log.debug(RETRIEVED_NBP_SERVICE_FOR_CODE_AND_DATE, code, dateAsString, nbpTableData);
        }
        catch (RestClientException e)
        {
            log.info(CommonUtils.UNEXPECTED_REQUEST_FAIL, e.getMessage());

            throw e;
        }

        result = buildDto(nbpTableData);

        return result;
    }

    /**
     * Calls NBP remote service for middle exchange rate for selected currency on a given day.
     *
     * @param code currency code according to ISO 4217 standard
     * @param date date
     * @return DTO for {@link ls.sandbox.nbp.model.NbpMiddleExchangeRate}
     */
    public NbpMiddleExchangeRateDto getMiddleExchangeRateFromNbpService(String code, Date date)
    {
        String dateAsString = CommonUtils.toStringFromDate(date);
        RestTemplate restTemplate = restTemplateBuilder.build();

        TableData nbpTableData = null;
        NbpMiddleExchangeRateDto result = null;

        log.info(CALLING_REMOTE_NBP_SERVICE);

        try
        {
            nbpTableData = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/A/{code}/{date}",
                                                     TableData.class, code, dateAsString);
            log.debug(RETRIEVED_NBP_SERVICE_FOR_CODE_AND_DATE, code, dateAsString, nbpTableData);
        }
        catch (RestClientException e)
        {
            log.info(CommonUtils.UNEXPECTED_REQUEST_FAIL, e.getMessage());

            throw e;
        }

        result = buildDto(nbpTableData);

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T extends ExchangeRateDto> T buildDto(TableData tableData)
    {
        T dto = null;

        if (null != tableData && null != tableData.getRates() && !tableData.getRates().isEmpty())
        {
            try
            {
                ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();

                Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];

                CurrencyDto currency = new CurrencyDto(tableData.getCode(), tableData.getCurrency());

                dto = type.getDeclaredConstructor(Long.class, Date.class, Double.class, CurrencyDto.class)
                        .newInstance(null,
                                     CommonUtils.parseDateFromString(tableData.getRates().get(0).getEffectiveDate()),
                                     Double.parseDouble(tableData.getRates().get(0).getAsk()), currency);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                log.fatal("Unexpected error while building exchange rate dto.", e);

                throw new RuntimeException(e); //@TODO introduce own exception (?)
            }
        }

        return dto;
    }
}
//------------------------------------------------------------------------------
