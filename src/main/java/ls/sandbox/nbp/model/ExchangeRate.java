//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.model
//
// File Name : ExchangeRate.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 26.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents some abstract NBP exchange rate.
 *
 * @author Lukasz.Stochlak
 */
@MappedSuperclass
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "code", "date" }) })
@Getter
@Setter
public abstract class ExchangeRate
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
        ExchangeRate that = (ExchangeRate) o;
        return getId() == that.getId() && Double.compare(getRate(), that.getRate()) == 0 && Objects.equals(getDate(),
                                                                                                           that.getDate())
               && Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getDate(), getRate(), getCurrency());
    }

    @Override
    public abstract String toString();
}
//------------------------------------------------------------------------------
