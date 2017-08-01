package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by esanchez on 7/19/2017.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_ORGA_ADMIN_X_FUNCI_PK")
@NamedQueries({
        @NamedQuery(name = "TvsOrgaAdminXFunciPk.findCodOrgaAdmiByIdeFunci", query = "SELECT  t.tvsOrgaAdminXFunciPkPk.codOrgaAdmi " +
                "FROM TvsOrgaAdminXFunciPk t " +
                "WHERE t.tvsOrgaAdminXFunciPkPk.ideFunci = :IDE_FUNCI")})
public class TvsOrgaAdminXFunciPk implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private TvsOrgaAdminXFunciPkPk tvsOrgaAdminXFunciPkPk;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;
    @JoinColumn(name = "IDE_FUNCI", referencedColumnName = "IDE_FUNCI", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionarios funcionarios;
}
