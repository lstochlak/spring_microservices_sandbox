//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : TableData.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.Value;

/**
 * Represents data returned by NBP service.
 *
 * @author Lukasz.Stochlak
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableData implements Serializable
{
    String currency;

    String code;

    List<RateValue> rates;
}
//------------------------------------------------------------------------------
