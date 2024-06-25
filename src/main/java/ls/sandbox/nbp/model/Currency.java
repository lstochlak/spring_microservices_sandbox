//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.model
//
// File Name : Currency.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 25.06.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * Represents selected currency.
 *
 * @author Lukasz.Stochlak
 */
@Entity(name = "Currency")
@Table(name = "currencies")
@Getter
@Setter
@ToString
public class Currency
{

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "currency", nullable = false)
    private String currency;

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

        Currency currency1 = (Currency) o;

        return Objects.equals(getCode(), currency1.getCode()) && Objects.equals(getCurrency(), currency1.getCurrency());
    }

    @Override
    public int hashCode()
    {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : Objects.hash(getCode(), getCurrency());
    }

}
//------------------------------------------------------------------------------
