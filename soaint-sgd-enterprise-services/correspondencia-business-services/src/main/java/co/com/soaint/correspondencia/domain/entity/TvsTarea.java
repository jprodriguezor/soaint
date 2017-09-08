package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by esanchez on 9/6/2017.
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "TVS_TAREA")
@NamedQueries({
        @NamedQuery(name = "TvsTarea.findAll", query = "SELECT t FROM TvsTarea t"),
        @NamedQuery(name = "TvsTarea.findByIdInstanciaProcesoAndIdTareaProceso", query = "SELECT t " +
                "FROM TvsTarea t " +
                "WHERE t.idInstanciaProceso = :ID_INSTANCIA_PROCESO AND t.idTareaProceso = :ID_TAREA_PROCESO"),
        @NamedQuery(name = "TvsTarea.existByIdInstanciaProcesoAndIdTareaProceso", query = "SELECT count(*) " +
                "FROM TvsTarea t " +
                "WHERE t.idInstanciaProceso = :ID_INSTANCIA_PROCESO AND t.idTareaProceso = :ID_TAREA_PROCESO"),
        @NamedQuery(name = "TvsTarea.updatePayloadByIdInstanciaProcesoAndIdTareaProceso", query = "UPDATE TvsTarea t " +
                "SET t.payload = :PAYLOAD " +
                "WHERE t.idInstanciaProceso = :ID_INSTANCIA_PROCESO AND t.idTareaProceso = :ID_TAREA_PROCESO")})
@javax.persistence.TableGenerator(name = "TVS_TAREA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_TAREA_SEQ", allocationSize = 1)
public class TvsTarea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_TAREA_GENERATOR")
    @Column(name = "IDE_TAREA")
    private BigInteger ideTarea;
    @Column(name = "ID_INSTANCIA_PROCESO")
    private String idInstanciaProceso;
    @Column(name = "ID_TAREA_PROCESO")
    private String idTareaProceso;
    @Lob
    @Column(name = "PAYLOAD")
    private Serializable payload;
}
