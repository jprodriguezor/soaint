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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "COR_PLANILLAS")
@NamedQueries({
    @NamedQuery(name = "CorPlanillas.findAll", query = "SELECT c FROM CorPlanillas c")})
@javax.persistence.TableGenerator(name = "COR_PLANILLAS_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_PLANILLAS_SEQ", allocationSize = 1)
public class CorPlanillas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_PLANILLAS_GENERATOR")
    @Column(name = "IDE_PLANILLA")
    private Long idePlanilla;
    @Column(name = "NRO_PLANILLA")
    private String nroPlanilla;
    @Column(name = "FEC_GENERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecGeneracion;
    @Column(name = "COD_TIPO_PLANILLA")
    private String codTipoPlanilla;
    @Column(name = "COD_FUNC_GENERA")
    private String codFuncGenera;
    @Column(name = "COD_SEDE_ORIGEN")
    private String codSedeOrigen;
    @Column(name = "COD_DEPENDENCIA_ORIGEN")
    private String codDependenciaOrigen;
    @Column(name = "COD_SEDE_DESTINO")
    private String codSedeDestino;
    @Column(name = "COD_DEPENDENCIA_DESTINO")
    private String codDependenciaDestino;
    @Column(name = "COD_CLASE_ENVIO")
    private String codClaseEnvio;
    @Column(name = "COD_MODALIDAD_ENVIO")
    private String codModalidadEnvio;
    @Column(name = "FECHA_CREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCrea;
    @Column(name = "COD_USUARIO_CREA")
    private String codUsuarioCrea;
    @Column(name = "FECHA_MODIF")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModif;
    @Column(name = "COD_USUARIO_MODIF")
    private String codUsuarioModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corPlanillas")
    private List<CorPlanAgen> corPlanAgenList;

}
