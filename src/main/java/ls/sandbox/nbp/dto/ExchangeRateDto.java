package ls.sandbox.nbp.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * DTO for {@link ls.sandbox.nbp.model.ExchangeRate}
 */
@Value
@NonFinal
public abstract class ExchangeRateDto implements Serializable
{
    long id;

    Date date;

    double rate;

    CurrencyDto currency;
}
