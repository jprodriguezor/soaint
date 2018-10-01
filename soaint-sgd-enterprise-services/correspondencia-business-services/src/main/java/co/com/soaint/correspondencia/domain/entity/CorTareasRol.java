package co.com.soaint.correspondencia.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Yosova on 9/24/2018.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "CorTareasRol.findRolByTask", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaDTO" +
                " (task.taskid,task.taskname,task.rolname) " +
                "FROM CorTareasRol task " +
                "WHERE TRIM(task.taskname)=trim(:INSTANCIA)")
})
public class CorTareasRol implements Serializable{

        private static final long serialVersionUID = 1L;
        @Basic
        @Column(name = "TASKNAME")
        private String taskname;
        @Basic
        @Column(name = "ROLNAME")
        private String rolname;
        @Id
        @Column(name = "TASKID")
        private Long taskid;

}
