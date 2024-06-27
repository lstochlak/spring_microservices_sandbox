//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.model
//
// File Name : NbpMiddleExchangeRate.java
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
 * Represents middle exchange rate in PLN of given currency (Mid value in table A of NBP service).
 *
 * @author Lukasz.Stochlak
 */
@Entity(name = "NbpMiddleExchangeRate")
@Table(name = "nbp_mids")
public class NbpMiddleExchangeRate extends ExchangeRate
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
        return "NbpMiddleExchangeRate{" + "id=" + getId() + ", date=" + getDate() + ", rate=" + getRate() + ", currency=" + getCurrency() + '}';
    }
}
//------------------------------------------------------------------------------
