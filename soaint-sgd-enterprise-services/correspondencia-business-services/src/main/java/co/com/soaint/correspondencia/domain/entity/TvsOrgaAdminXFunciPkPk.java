package co.com.soaint.correspondencia.domain.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by esanchez on 7/19/2017.
 */
@Embeddable
@Data
public class TvsOrgaAdminXFunciPkPk implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "COD_ORGA_ADMI")
    @Basic(optional = false)
    private String codOrgaAdmi;
    @Column(name = "IDE_FUNCI")
    @Basic(optional = false)
    private BigInteger ideFunci;
}
