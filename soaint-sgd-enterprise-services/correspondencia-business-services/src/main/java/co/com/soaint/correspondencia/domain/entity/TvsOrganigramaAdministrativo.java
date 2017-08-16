/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author jrodriguez
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_ORGANIGRAMA_ADMINISTRATIVO")
@NamedQueries({
        @NamedQuery(name = "TvsOrganigramaAdministrativo.findAll", query = "SELECT t FROM TvsOrganigramaAdministrativo t"),
    @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoRayz", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
            "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.indEsActivo, t.descOrg, t.codNivel) " +
            "FROM TvsOrganigramaAdministrativo t " +
            "WHERE t.ideOrgaAdminPadre IS NULL"),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementoByIdeOrgaAdmin", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t.indEsActivo, t.descOrg, t.codNivel) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "WHERE t.ideOrgaAdmin = :IDE_ORGA_ADMIN"),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarDescendientesDirectos", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t1.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t1.codNivel) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrganigramaAdministrativo t1 ON t.ideOrgaAdminPadre = t1.ideOrgaAdmin " +
                "WHERE t.ideOrgaAdminPadre = :ID_PADRE"),
        @NamedQuery(name = "TvsOrganigramaAdministrativo.consultarElementosByCodOrgList", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO" +
                "(t.ideOrgaAdmin, t.codOrg, t.nomOrg, t.ideOrgaAdminPadre, t1.nomOrg, t.indEsActivo, t.codNivel, t.descOrg, t1.codNivel) " +
                "FROM TvsOrganigramaAdministrativo t " +
                "INNER JOIN TvsOrganigramaAdministrativo t1 ON t.ideOrgaAdminPadre = t1.ideOrgaAdmin " +
                "WHERE t.codOrg IN :COD_ORG_LIST")
        })
@Cacheable(true)
public class TvsOrganigramaAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDE_ORGA_ADMIN")
    private BigInteger ideOrgaAdmin;
    @Basic(optional = false)
    @Column(name = "COD_ORG")
    private String codOrg;
    @Basic(optional = false)
    @Column(name = "NOM_ORG")
    private String nomOrg;
    @Basic(optional = false)
    @Column(name = "DESC_ORG")
    private String descOrg;
    @Basic(optional = false)
    @Column(name = "IND_ES_ACTIVO")
    private String indEsActivo;
    @Column(name = "IDE_DIRECCION")
    private String ideDireccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "IDE_PLAN_RESPONSABLE")
    private BigDecimal idePlanResponsable;
    @Column(name = "IDE_ORGA_ADMIN_PADRE")
    private String ideOrgaAdminPadre;
    @Column(name = "COD_NIVEL")
    private String codNivel;
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "IDE_USUARIO_CREO")
    private BigInteger ideUsuarioCreo;
    @Basic(optional = false)
    @Column(name = "IDE_USUARIO_CAMBIO")
    private BigInteger ideUsuarioCambio;
    @Column(name = "FEC_CAMBIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCambio;
    @Column(name = "VAL_SISTEMA")
    private String valSistema;
    @Column(name = "VAL_VERSION")
    private String valVersion;
}
