package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by esaliaga on 11/01/2018.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TVS_PLANTILLA_METADATO")
@NamedQueries({
        @NamedQuery(name = "TvsPlantillaMestadato.findByIdePlantilla", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PlantillaMetadatoDTO " +
                "(t.idePlantillaMetadato, t.codMetadato, t.nombMetadato) " +
                "FROM TvsPlantillaMetadato t " +
                "WHERE t.plantilla.idePlantilla = :IDE_PLANTILLA")})
@javax.persistence.TableGenerator(name = "TVS_PLANTILLA_METADATO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_PLANTILLA_METADATO_SEQ", allocationSize = 1)
public class TvsPlantillaMetadato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_PLANTILLA_METADATO_GENERATOR")
    @Column(name = "IDE_PLANTILLA_METADATO")
    private BigInteger idePlantillaMetadato;
    @Basic(optional = false)
    @Column(name = "COD_METADATO")
    private String codMetadato;
    @Basic(optional = false)
    @Column(name = "NOMB_METADATO")
    private String nombMetadato;
    @JoinColumn(name = "IDE_PLANTILLA", referencedColumnName = "IDE_PLANTILLA")
    @ManyToOne(optional = false)
    private TvsPlantilla plantilla;
}
