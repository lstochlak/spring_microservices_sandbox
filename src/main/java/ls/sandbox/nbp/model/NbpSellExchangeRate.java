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

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * Represents sell exchange rate in PLN of given currency (Ask value in table C of NBP service).
 *
 * @author Lukasz.Stochlak
 */
@Entity(name = "NbpSellExchangeRate")
@Table(name = "nbp_sell")
@Getter
@Setter
@ToString
public class NbpSellExchangeRate
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "rate", nullable = false)
    private double rate;

    @ManyToOne(targetEntity = Currency.class, optional = false)
    @JoinColumn(name = "curr_code")
    private Currency currency;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
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

        NbpSellExchangeRate that = (NbpSellExchangeRate) o;
        return getId() == that.getId() && Double.compare(getRate(), that.getRate()) == 0 && Objects.equals(getDate(),
                                                                                                           that.getDate())
               && Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode()
    {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : Objects.hash(getId(), getDate(), getRate(), getCurrency());
    }
}
//------------------------------------------------------------------------------
