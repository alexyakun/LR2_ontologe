package equip;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoltageLevel {
    private BaseVoltage baseVoltage;
    private Substation substation;
    private String mRID;

    public VoltageLevel (BaseVoltage baseVoltage, Substation substation, String mRID) {
        this.mRID = mRID;
        this.baseVoltage = baseVoltage;
        this.substation = substation;
    }
}
