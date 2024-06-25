//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.repository
//
// File Name : NbpSellExchangeRateRepository.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.repository;

import java.util.Date;
import ls.sandbox.nbp.model.NbpSellExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for {@link ls.sandbox.nbp.model.NbpSellExchangeRate}
 *
 * @author Lukasz.Stochlak
 */
public interface NbpSellExchangeRateRepository extends JpaRepository<NbpSellExchangeRate, Long>
{
    @Query("SELECT c FROM NbpSellExchangeRate c WHERE c.currency.code = (:code) AND c.date = (:date)")
    NbpSellExchangeRate findByCodeAndDate(@Param("code") String code, @Param("date") Date date);
}
