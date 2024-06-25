//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.dto
//
// File Name : NbpRateValue.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Single rate value from NBP table (response from NBP service - optimized to be used for both tables A and C).
 *
 * @author Lukasz.Stochlak
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpRateValue
{
    private String effectiveDate;

    private String mid;

    private String ask;
}
//------------------------------------------------------------------------------
