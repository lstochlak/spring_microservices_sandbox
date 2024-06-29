package ls.sandbox.nbp.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * DTO for {@link ls.sandbox.nbp.model.ExchangeRate}
 */
@Value
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@NonFinal
public abstract class ExchangeRateDto implements Serializable
{
    Long id;

    Date date;

    Double rate;

    CurrencyDto currency;
}
