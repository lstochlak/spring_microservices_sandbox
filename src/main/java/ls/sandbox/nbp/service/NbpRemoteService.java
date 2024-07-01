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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    protected static final String URL_NBP_TABLE_A_MIDDLE_EXCHANGE_RATE =
            "http://api.nbp.pl/api/exchangerates/rates/A/{code}/{date}";

    protected static final String URL_NBP_TABLE_C_SELL_EXCHANGE_RATE =
            "http://api.nbp.pl/api/exchangerates/rates/C/{code}/{date}";

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
        return geExchangeRateFromNbpService(code, date, URL_NBP_TABLE_C_SELL_EXCHANGE_RATE,
                                            NbpSellExchangeRateDto.class);
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
        return geExchangeRateFromNbpService(code, date, URL_NBP_TABLE_A_MIDDLE_EXCHANGE_RATE,
                                            NbpMiddleExchangeRateDto.class);
    }

    private <T extends ExchangeRateDto> T geExchangeRateFromNbpService(String code, Date date, String url,
                                                                       Class<T> dtoClass)
    {
        String dateAsString = CommonUtils.toStringFromDate(date);
        RestTemplate restTemplate = restTemplateBuilder.build();

        TableData nbpTableData = null;
        T result = null;

        log.info(CALLING_REMOTE_NBP_SERVICE);

        ResponseEntity<TableData> responseEntity = null;
        try
        {
            responseEntity = restTemplate.getForEntity(url, TableData.class, code, dateAsString);

            if (responseEntity.getStatusCode().equals(HttpStatus.OK) && null != responseEntity.getBody())
            {
                nbpTableData = responseEntity.getBody();

                log.debug(RETRIEVED_NBP_SERVICE_FOR_CODE_AND_DATE, code, dateAsString, nbpTableData);

                result = buildDtoFromTableData(nbpTableData, dtoClass);
            }
        }
        catch (HttpClientErrorException.NotFound e)
        {
            log.debug(
                    "404 response received from NBP remote service - currency exchange rate not found! code={} dte={}",
                    code, dateAsString);
        }
        catch (HttpClientErrorException e)
        {
            log.debug("Unexpected {} response received from NBP remote service! code={} dte={}", e.getStatusCode(),
                      code, dateAsString);
        }
        catch (RestClientException e)
        {
            log.debug(CommonUtils.UNEXPECTED_REQUEST_FAIL, e.getMessage());
            throw new UnexpectedServiceException(e);
        }

        return result;
    }

    private <T extends ExchangeRateDto> T buildDtoFromTableData(TableData tableData, Class<T> dtoClass)
    {
        T dto = null;

        if (null != tableData && null != tableData.getRates() && !tableData.getRates().isEmpty())
        {
            try
            {
                CurrencyDto currency = new CurrencyDto(tableData.getCode(), tableData.getCurrency());

                dto = dtoClass.getDeclaredConstructor(Long.class, Date.class, Double.class, CurrencyDto.class)
                        .newInstance(null,
                                     CommonUtils.parseDateFromString(tableData.getRates().get(0).getEffectiveDate()),
                                     Double.parseDouble(
                                             null != tableData.getRates().get(0).getAsk() ? tableData.getRates().get(0)
                                                     .getAsk() : tableData.getRates().get(0).getMid()),
                                     currency);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                log.fatal("Unexpected error while building exchange rate dto!", e);

                throw new UnexpectedServiceException(e);
            }
        }

        return dto;
    }
}
//------------------------------------------------------------------------------
