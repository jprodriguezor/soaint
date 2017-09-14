package co.com.soaint.bpm.services.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Arce on 9/7/2017.
 */
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BAMTASKSUMMARY")
@NamedQueries({
        @NamedQuery(name = "BamTaskSummary.findTaskComplete", query = "SELECT NEW co.com.soaint.foundation.canonical.bpm.RespuestaTareaBamDTO" +
                " (bam.pk,bam.createddate,bam.duration,bam.enddate,bam.processinstanceid,bam.startdate,bam.status,bam.taskid,bam.taskname,bam.userid,bam.optlock) " +
                "FROM BamtasksummaryEntity bam " +
                "WHERE TRIM(bam.status) = TRIM(:ESTADO) AND TRIM(bam.userid)=trim(:USUARIO)")})

public class BamtasksummaryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "PK")
    private int pk;
    @Basic
    @Column(name = "CREATEDDATE")
    private Date createddate;
    @Basic
    @Column(name = "DURATION")
    private int duration;
    @Basic
    @Column(name = "ENDDATE")
    private Date enddate;
    @Basic
    @Column(name = "PROCESSINSTANCEID")
    private int processinstanceid;
    @Basic
    @Column(name = "STARTDATE")
    private Date startdate;
    @Basic
    @Column(name = "STATUS")
    private String status;
    @Basic
    @Column(name = "TASKID")
    private int taskid;
    @Basic
    @Column(name = "TASKNAME")
    private String taskname;
    @Basic
    @Column(name = "USERID")
    private String userid;
    @Basic
    @Column(name = "OPTLOCK")
    private int optlock;

}
