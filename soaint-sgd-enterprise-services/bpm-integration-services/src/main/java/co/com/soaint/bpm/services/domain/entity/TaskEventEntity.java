package co.com.soaint.bpm.services.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Arce on 10/19/2017.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASKEVENT")
@NamedQueries({
        @NamedQuery(name = "TaskEventEntity.findTaskByUser", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO" +
                " (task.type,count(*)) " +
                "FROM TaskEventEntity task " +
                "WHERE TRIM(task.userid)=trim(:USUARIO) " +
                "GROUP BY task.type")})

public class TaskEventEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "LOGTIME")
    private Date logtime;
    @Basic
    @Column(name = "MESSAGE")
    private String message;
    @Basic
    @Column(name = "PROCESSINSTANCEID")
    private int processinstanceid;
    @Basic
    @Column(name = "TASKID")
    private int taskid;
    @Basic
    @Column(name = "TYPE")
    private String type;
    @Basic
    @Column(name = "USERID")
    private String userid;
    @Basic
    @Column(name = "OPTLOCK")
    private int optlock;
    @Basic
    @Column(name = "WORKITEMID")
    private int workitemd;

}
