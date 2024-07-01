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
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for {@link ls.sandbox.nbp.model.NbpMiddleExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class NbpMiddleExchangeRateDto extends ExchangeRateDto
{
    /**
     * All parameters constructor.
     *
     * @param id id.
     * @param date date.
     * @param rate rate
     * @param currency currency {@link ls.sandbox.nbp.dto.CurrencyDto}
     */
    public NbpMiddleExchangeRateDto(Long id, Date date, Double rate, CurrencyDto currency)
    {
        super(id, date, rate, currency);
    }
}
