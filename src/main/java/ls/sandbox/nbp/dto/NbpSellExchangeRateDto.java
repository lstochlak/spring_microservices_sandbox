//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : NbpSellExchangeRateDto.java
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
 * DTO for {@link ls.sandbox.nbp.model.NbpSellExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NbpSellExchangeRateDto  extends ExchangeRateDto
{
    public NbpSellExchangeRateDto(Long id, Date date, Double rate, CurrencyDto currency)
    {
        super(id, date, rate, currency);
    }
}
