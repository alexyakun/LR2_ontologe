package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerTransformer {
    private String mRID;
    private String name;
    private boolean isPartOfGeneratorUnit;
    private double magBaseU;
    private double bmagSat;
    private double magSatFlux;
    private BaseVoltage baseVoltage;
    private VoltageLevel equipmentContainer;
}
