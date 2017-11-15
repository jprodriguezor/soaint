package co.com.soaint.correspondencia.domain.entity;

import lombok.Data;

import javax.persistence.*;
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
    @JoinColumn(name = "IDE_FUNCI", referencedColumnName = "IDE_FUNCI")
    @ManyToOne
    private Funcionarios funcionario;
}
