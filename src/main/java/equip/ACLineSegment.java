package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ACLineSegment {
    private String mRID;
    private String name;
    private double length;
    private double r;
    private double r0;
    private double x;
    private double x0;
    private double b0ch;
    private double bch;
    private double ratedActivePower;
    private boolean userConcentratedParameters;
    private BaseVoltage baseVoltage;
    private Line line;
}
