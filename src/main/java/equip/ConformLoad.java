package equip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConformLoad {
    private String mRID;
    private String name;
    private BaseVoltage baseVoltage;
    private EquipmentContainer equipmentContainer;
    private ActivePower pfixed;
    private ReactivePower qfixed;
}
