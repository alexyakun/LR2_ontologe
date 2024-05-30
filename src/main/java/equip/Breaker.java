package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Breaker {
    private String mRID;
    private String name;
    private boolean open;
    private double ratedCurrent;
    private BaseVoltage baseVoltage;
    private VoltageLevel voltageLevel;
}
