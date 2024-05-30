package Converter;

import Converter.parsers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CimConverter {
    @SneakyThrows
    public static void main(String[] args) {
        File initialFile = new File("src/main/resources/lr1_scheme.xml");
        InputStream targetStream = new FileInputStream(initialFile);
        Model model = Rio.parse(targetStream,"http://iec.ch/TC57/2013/CIM-schema-cim16#", RDFFormat.RDFXML);
        Repository repository = new SailRepository(new MemoryStore());
        RepositoryConnection connection = repository.getConnection();
        connection.add(model);
        RDF rdf = new RDF();
        ElementParser substation = new SubstationParser();
        substation.parse(rdf, connection);
        ElementParser line = new LineParser();
        line.parse(rdf, connection);
        BaseFrequencyParser frequencyParser = new BaseFrequencyParser();
        frequencyParser.parse(rdf, connection);
        BaseVoltageParser baseVoltageParser = new BaseVoltageParser();
        baseVoltageParser.parse(rdf, connection);
        VoltageLevelParser voltageLevelParser = new VoltageLevelParser();
        voltageLevelParser.parse(rdf, connection);
        EquivalentInjectionParser equivalentInjectionParser = new EquivalentInjectionParser();
        equivalentInjectionParser.parse(rdf, connection);
        ACLineSegmentParser acLineSegmentParser = new ACLineSegmentParser();
        acLineSegmentParser.parse(rdf,connection);
        BreakerParser breakerParser = new BreakerParser();
        breakerParser.parse(rdf, connection);
        BusbarSectionParser busbarSectionParser = new BusbarSectionParser();
        busbarSectionParser.parse(rdf, connection);
        LoadResponseCharacteristicParser loadResponseCharacteristicParser = new LoadResponseCharacteristicParser();
        loadResponseCharacteristicParser.parse(rdf, connection);
        EnergyConsumerParser energyConsumerParser = new EnergyConsumerParser();
        energyConsumerParser.parse(rdf, connection);
        PowerTransformerParser powerTransformerParser = new PowerTransformerParser();
        powerTransformerParser.parse(rdf, connection);
        PowerTransformerEndParser powerTransformerEndParser = new PowerTransformerEndParser();
        powerTransformerEndParser.parse(rdf, connection);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                new File("src/main/resources/jsonModel1.json"),rdf);
        System.out.println();
    }
}
