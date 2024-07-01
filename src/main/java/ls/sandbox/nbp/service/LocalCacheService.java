//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.service
//
// File Name : LocalCacheService.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 28.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import ls.sandbox.nbp.dto.CurrencyDto;
import ls.sandbox.nbp.dto.ExchangeRateDto;
import ls.sandbox.nbp.dto.NbpMiddleExchangeRateDto;
import ls.sandbox.nbp.dto.NbpSellExchangeRateDto;
import ls.sandbox.nbp.model.Currency;
import ls.sandbox.nbp.model.ExchangeRate;
import ls.sandbox.nbp.model.NbpMiddleExchangeRate;
import ls.sandbox.nbp.model.NbpSellExchangeRate;
import ls.sandbox.nbp.repository.CurrencyRepository;
import ls.sandbox.nbp.repository.ExchangeRateRepository;
import ls.sandbox.nbp.repository.NbpMiddleExchangeRateRepository;
import ls.sandbox.nbp.repository.NbpSellExchangeRateRepository;
import ls.sandbox.nbp.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for local currency cache.
 *
 * @author Lukasz.Stochlak
 */
@Service
@Log4j2
public class LocalCacheService
{

    @Autowired
    private NbpSellExchangeRateRepository nbpSellExchangeRateRepository;

    @Autowired
    private NbpMiddleExchangeRateRepository nbpMiddleExchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    /**
     * Saves a sell rate in local cache.
     *
     * @param dto DTO object {@link ls.sandbox.nbp.dto.NbpSellExchangeRateDto}.
     */
    public void cacheSellExchangeRate(NbpSellExchangeRateDto dto)
    {
        cacheExchangeRate(dto, new NbpSellExchangeRate(), nbpSellExchangeRateRepository);
    }

    /**
     * Saves a middle rate in local cache.
     *
     * @param dto DTO object {@link ls.sandbox.nbp.dto.NbpMiddleExchangeRateDto}.
     */
    public void cacheMiddleExchangeRate(NbpMiddleExchangeRateDto dto)
    {
        cacheExchangeRate(dto, new NbpMiddleExchangeRate(), nbpMiddleExchangeRateRepository);
    }

    /**
     * Saves echange rate in local cache.
     *
     * @param dto DTO object, extending {@link ls.sandbox.nbp.dto.ExchangeRateDto}, containing data to be saved in cache.
     * @param obj class T model object.
     * @param repo repository for T class objects (extending {@link ls.sandbox.nbp.repository.ExchangeRateRepository}).
     * @param <T> model object class extending {@link ls.sandbox.nbp.model.ExchangeRate}.
     */
    private <T extends ExchangeRate> void cacheExchangeRate(ExchangeRateDto dto, T obj, ExchangeRateRepository<T> repo)
    {
        Currency currency = new Currency();
        currency.setCode(dto.getCurrency().getCode());
        currency.setCurrency(dto.getCurrency().getCurrency());

        obj.setCurrency(currency);
        obj.setDate(dto.getDate());
        obj.setRate(dto.getRate());

        currencyRepository.saveAndFlush(currency);
        repo.saveAndFlush(obj);
    }

    /**
     * Looks for a sell exchange rate of the selected currency on a given day.
     *
     * @param code currency code according to ISO 4217 standard.
     * @param date date.
     * @return DTO object {@link ls.sandbox.nbp.dto.NbpSellExchangeRateDto} from local cache.
     */
    public NbpSellExchangeRateDto findSellExchangeRate(String code, Date date)
    {
        NbpSellExchangeRateDto localRateDto = null;

        NbpSellExchangeRate localRate = findExchangeRate(code, date, nbpSellExchangeRateRepository);

        localRateDto = buildDtoFromExchangeRate(localRate, NbpSellExchangeRateDto.class);

        return localRateDto;
    }

    /**
     * Looks for a middle exchange rate of the selected currency on a given day.
     *
     * @param code currency code according to ISO 4217 standard.
     * @param date date.
     * @return DTO object {@link ls.sandbox.nbp.dto.NbpMiddleExchangeRateDto}
     */
    public NbpMiddleExchangeRateDto findMiddleExchangeRate(String code, Date date)
    {
        NbpMiddleExchangeRateDto localRateDto = null;

        NbpMiddleExchangeRate localRate = findExchangeRate(code, date, nbpMiddleExchangeRateRepository);

        localRateDto = buildDtoFromExchangeRate(localRate, NbpMiddleExchangeRateDto.class);

        return localRateDto;
    }

    /**
     * Looks for exchange rate in local database.
     *
     * @param code currency code according to ISO 4217 standard.
     * @param date date.
     * @param repo repository for T class objects (extending {@link ls.sandbox.nbp.repository.ExchangeRateRepository}).
     * @return model object extending {@link ls.sandbox.nbp.model.ExchangeRate} found in local cache.
     * @param <T> model object class extending {@link ls.sandbox.nbp.model.ExchangeRate}.
     */
    private <T extends ExchangeRate> T findExchangeRate(String code, Date date, ExchangeRateRepository<T> repo)
    {
        T localRate = null;

        try
        {
            localRate = repo.findByCodeAndDate(code, date);

        }
        catch (EntityNotFoundException e)
        {
            String dateAsString = CommonUtils.toStringFromDate(date);

            log.debug("Exchange rate for code={} and date={} not found in local DB.", code, dateAsString);
        }

        return localRate;
    }

    /**
     * Builds DTO from model object.
     *
     * @param rate model object.
     * @param dtoClass expected DTO class.
     * @return built DTO object.
     * @param <T> DTO object class extending {@link ls.sandbox.nbp.dto.ExchangeRateDto}.
     */
    private <T extends ExchangeRateDto> T buildDtoFromExchangeRate(ExchangeRate rate, Class<T> dtoClass)
    {
        T dto = null;

        if (null != rate && null != rate.getCurrency())
        {
            try
            {
                CurrencyDto currency = new CurrencyDto(rate.getCurrency().getCode(), rate.getCurrency().getCurrency());

                dto = dtoClass.getDeclaredConstructor(Long.class, Date.class, Double.class, CurrencyDto.class)
                        .newInstance(rate.getId(), rate.getDate(), rate.getRate(), currency);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                   NoSuchMethodException e)
            {
                log.fatal("Unexpected error while building exchange rate dto.", e);

                throw new UnexpectedServiceException(e);
            }
        }

        return dto;
    }
}
//------------------------------------------------------------------------------
