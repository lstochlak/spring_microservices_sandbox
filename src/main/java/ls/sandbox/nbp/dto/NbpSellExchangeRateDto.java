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

import java.io.Serializable;
import java.util.Date;
import lombok.Value;
import ls.sandbox.nbp.model.NbpSellExchangeRate;

/**
 * DTO for {@link ls.sandbox.nbp.model.NbpSellExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
@Value
public class NbpSellExchangeRateDto implements Serializable
{
    long id;

    Date date;

    double rate;

    CurrencyDto currency;
}
