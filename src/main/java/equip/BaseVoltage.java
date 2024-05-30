package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseVoltage extends RDFElement{

    private String name;
    private String mRID;
    private Double nominalVoltage;
}
