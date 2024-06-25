//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : CurrencyDto.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.dto;

import java.io.Serializable;
import lombok.Value;

/**
 * DTO for {@link ls.sandbox.nbp.model.Currency}
 *
 * @author Lukasz.Stochlak
 */
@Value
public class CurrencyDto implements Serializable
{
    String code;

    String currency;
}
