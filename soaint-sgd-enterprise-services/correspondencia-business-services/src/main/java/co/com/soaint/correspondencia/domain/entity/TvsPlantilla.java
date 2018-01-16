package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by esaliaga on 11/01/2018.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_PLANTILLA")
@NamedQueries({
        @NamedQuery(name = "TvsPlantilla.findByCodClasificacionAndEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PlantillaDTO " +
                "(t.idePlantilla, t.referencia, t.tipoDoc.codigo, t.codTipoUbicacion, t.ubicacion) " +
                "FROM TvsPlantilla t " +
                "WHERE t.tipoDoc.codigo = :COD_TIPO_DOC AND t.estado = :ESTADO"),
        @NamedQuery(name = "TvsPlantilla.findTipoDocByEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ConstanteDTO " +
                "(t.tipoDoc.ideConst, t.tipoDoc.codigo, t.tipoDoc.nombre, t.tipoDoc.codPadre) " +
                "FROM TvsPlantilla t " +
                "WHERE t.estado = :ESTADO")})
@javax.persistence.TableGenerator(name = "TVS_PLANTILLA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_PLANTILLA_SEQ", allocationSize = 1)
public class TvsPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_PLANTILLA_GENERATOR")
    @Column(name = "IDE_PLANTILLA")
    private BigInteger idePlantilla;
    @Basic(optional = false)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Basic(optional = false)
    @Column(name = "COD_TIPO_UBICACION")
    private String codTipoUbicacion;
    @Basic(optional = false)
    @Column(name = "UBICACION")
    private String ubicacion;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plantilla")
    private List<TvsPlantillaMetadato> metadatos;
    @JoinColumn(name = "COD_TIPO_DOC", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private TvsConstantes tipoDoc;
}
