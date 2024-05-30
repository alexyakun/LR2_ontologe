package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumer {
    private String mRID;
    private String name;
    private boolean grounded;
    private double pfixed;
    private double p;
    private double qfixed;
    private double q;
    private double ratedVoltage;
    private BaseVoltage baseVoltage;
    private VoltageLevel voltageLevel;
    private  LoadResponseCharacteristic loadResponseCharacteristic;
}
