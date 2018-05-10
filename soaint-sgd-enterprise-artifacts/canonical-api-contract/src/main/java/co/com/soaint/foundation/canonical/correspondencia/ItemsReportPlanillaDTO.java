package co.com.soaint.foundation.canonical.correspondencia;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * Soaint Generic Artifact
 * Created:05-Sep-2017
 * Author: esachez
 * Type: JAVA class Artifact
 * Purpose: DTO - Model Artifact
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
@ToString
public class ItemsReportPlanillaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<ItemReportPlanillaDTO> itemsPlanilla;
}
