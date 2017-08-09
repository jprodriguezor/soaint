package co.com.soaint.ecm.domain.entity;

import lombok.Data;
import org.apache.chemistry.opencmis.client.api.Session;

/**
 * Created by Dasiel
 */
@Data
public class  Conexion {
    Session session= null;
}
