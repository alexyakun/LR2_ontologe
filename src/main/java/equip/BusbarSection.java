package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusbarSection {
    private String mRID;
    private String name;
    private BaseVoltage baseVoltage;
    private VoltageLevel voltageLevel;
}
