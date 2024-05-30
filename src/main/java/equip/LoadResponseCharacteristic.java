package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadResponseCharacteristic {
    private String mRID;
    private double pVoltageExponent;
    private double qVoltageExponent;
    private double qConstantPower;
}
