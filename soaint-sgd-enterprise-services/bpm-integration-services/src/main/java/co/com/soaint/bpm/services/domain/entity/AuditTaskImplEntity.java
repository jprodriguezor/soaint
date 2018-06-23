package co.com.soaint.bpm.services.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Yosova on 6/7/2018.
 */

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUDITTASKIMPL")
@NamedQueries({
        @NamedQuery(name = "AuditTaskImplEntity.findProcessById", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaDigitalizarDTO" +
                " (task.deploymentid,task.processid,task.processinstanceid) " +
                "FROM AuditTaskImplEntity task " +
                "WHERE TRIM(task.processinstanceid)=trim(:INSTANCIA)")})
public class AuditTaskImplEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Basic
    @Column(name = "DEPLOYMENTID")
    private String deploymentid;
    @Id
    @Column(name = "PROCESSID")
    private String processid;
    @Basic
    @Column(name = "PROCESSINSTANCEID")
    private Long processinstanceid;

}
