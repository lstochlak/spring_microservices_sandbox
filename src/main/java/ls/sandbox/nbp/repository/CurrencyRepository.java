//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.repository
//
// File Name : CurrencyRepository.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.repository;

import ls.sandbox.nbp.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link ls.sandbox.nbp.model.Currency}
 *
 * @author Lukasz.Stochlak
 */
public interface CurrencyRepository extends JpaRepository<Currency, String>
{
}
//------------------------------------------------------------------------------
