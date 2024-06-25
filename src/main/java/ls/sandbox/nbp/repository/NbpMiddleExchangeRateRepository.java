//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.repository
//
// File Name : NbpMiddleExchangeRateRepository.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.repository;

import java.util.Date;
import ls.sandbox.nbp.model.NbpMiddleExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for {@link ls.sandbox.nbp.model.NbpMiddleExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
public interface NbpMiddleExchangeRateRepository extends JpaRepository<NbpMiddleExchangeRate, Long>
{
    @Query("SELECT c FROM NbpMiddleExchangeRate c WHERE c.currency.code = (:code) AND c.date = (:date)")
    NbpMiddleExchangeRate findByCodeAndDate(@Param("code") String code, @Param("date") Date date);
}
