//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : RateValue.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Value;

/**
 * Single rate value from NBP table (response from NBP service - optimized to be used for both tables A and C).
 *
 * @author Lukasz.Stochlak
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class RateValue implements Serializable
{
    String effectiveDate;

    String mid;

    String ask;
}
//------------------------------------------------------------------------------
