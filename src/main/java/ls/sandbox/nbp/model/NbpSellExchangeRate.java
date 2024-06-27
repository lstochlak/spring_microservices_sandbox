//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.model
//
// File Name : NbpSellExchangeRate.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.proxy.HibernateProxy;

/**
 * Represents sell exchange rate in PLN of given currency (Ask value in table C of NBP service).
 *
 * @author Lukasz.Stochlak
 */
@Entity(name = "NbpSellExchangeRate")
@Table(name = "nbp_sell")
public class NbpSellExchangeRate  extends ExchangeRate
{
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null)
        {
            return false;
        }
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                                            : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
        {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode()
    {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : super.hashCode();
    }

    @Override
    public String toString()
    {
        return "NbpSellExchangeRate{" + "id=" + getId() + ", date=" + getDate() + ", rate=" + getRate() + ", currency=" + getCurrency() + '}';
    }
}
//------------------------------------------------------------------------------
