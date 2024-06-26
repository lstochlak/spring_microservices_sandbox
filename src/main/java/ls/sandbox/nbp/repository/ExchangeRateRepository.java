//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.repository
//
// File Name : ExchangeRateRepository.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 26.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.repository;

import java.util.Date;
import ls.sandbox.nbp.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 * Abstract repository for {@link ls.sandbox.nbp.model.ExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
@NoRepositoryBean
public interface ExchangeRateRepository<T extends ExchangeRate> extends JpaRepository<T, Long>
{
    @Query(value = "SELECT c FROM #{#entityName} c WHERE c.currency.code = (:code) AND c.date = (:date)")
    T findByCodeAndDate(@Param("code") String code, @Param("date") Date date);
}
