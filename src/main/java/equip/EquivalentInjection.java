package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquivalentInjection {
    private String mRID;
    private String name;
    private BaseVoltage baseVoltage;
    private VoltageLevel equipmentContainer;
    private double r;
    private double r2;
    private double x;
    private double x2;
    private double r0;
    private double x0;

}
