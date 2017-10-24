package co.com.soaint.foundation.canonical.bpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Arce on 9/13/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@XmlRootElement(namespace = "http://soaint.com/domain-artifacts/respuestatarea/1.0.0")
public class RespuestaTareaBamDTO {

    private int pk;
    private Date eddate;
    private int duration;
    private Date enddate;
    private int processinstanceid;
    private Date startdate;
    private String status;
    private int taskid;
    private String taskname;
    private String userid;
    private int optlock;
    private Long cantidad;


    public RespuestaTareaBamDTO(String status, Long cantidad) {
        this.status = status;
        this.cantidad = cantidad;
    }

    public RespuestaTareaBamDTO(int pk, Date eddate, int duration, Date enddate, int processinstanceid, Date startdate, String status, int taskid, String taskname, String userid, int optlock) {
        this.pk = pk;
        this.eddate = eddate;
        this.duration = duration;
        this.enddate = enddate;
        this.processinstanceid = processinstanceid;
        this.startdate = startdate;
        this.status = status;
        this.taskid = taskid;
        this.taskname = taskname;
        this.userid = userid;
        this.optlock = optlock;
    }
}



