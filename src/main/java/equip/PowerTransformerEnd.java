package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerTransformerEnd {
    private String mRID;
    private String name;
    private int endNumber;
    private int phaseAndClock;
    private boolean grounded;
    private double ratedU;
    private double ratedS;
    private double g;
    private double b;
    private boolean doesSaturationExist;
    private double x;
    private double r;
    private BaseVoltage baseVoltage;
    private PowerTransformer powerTransformer;
    private WindingConnection connectionKind;

}
