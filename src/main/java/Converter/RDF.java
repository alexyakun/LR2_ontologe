package Converter;

import equip.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class RDF {
    private List<Substation> substations = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private List<BaseVoltage> baseVoltages = new ArrayList<>();
    private List<BaseFrequency> baseFrequency = new ArrayList<>();
    private List<VoltageLevel> voltageLevels = new ArrayList<>();
    private List<PowerTransformer> powerTransformers = new ArrayList<>();
    private List<EquivalentInjection> equivalentInjections = new ArrayList<>();
    private List<PowerTransformerEnd> powerTransformerEnds = new ArrayList<>();
    private List<ACLineSegment> acLineSegments = new ArrayList<>();
    private List<ConformLoad> conformLoads = new ArrayList<>();
    private List<Breaker> breakers = new ArrayList<>();
    private List<BusbarSection> busbarSections = new ArrayList<>();
    private List<LoadResponseCharacteristic> loadResponseCharacteristics = new ArrayList<>();
    private List<EnergyConsumer> energyConsumers = new ArrayList<>();
}
