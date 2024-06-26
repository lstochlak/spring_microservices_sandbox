//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : NbpMiddleExchangeRateDto.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.dto;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DTO for {@link ls.sandbox.nbp.model.NbpMiddleExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NbpMiddleExchangeRateDto extends ExchangeRateDto
{
    public NbpMiddleExchangeRateDto(long id, Date date, double rate, CurrencyDto currency)
    {
        super(id, date, rate, currency);
    }
}
