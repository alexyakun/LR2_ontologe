package Converter;

import equip.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllObjects {

    private List<Substation> substation = new ArrayList<>();
    private List<BaseVoltage> baseVoltages = new ArrayList<>();
    private List<BaseFrequency> baseFrequency = new ArrayList<>();
    private List<VoltageLevel> voltageLevels = new ArrayList<>();
    private List<EquivalentInjection> equivalentInjections = new ArrayList<>();
    private List<ACLineSegment> acLineSegments = new ArrayList<>();
    private List<ConformLoad> conformLoads = new ArrayList<>();
    private List<PowerTransformer> powerTransformers = new ArrayList<>();
    private List<PowerTransformerEnd> powerTransformerEnds = new ArrayList<>();
}
