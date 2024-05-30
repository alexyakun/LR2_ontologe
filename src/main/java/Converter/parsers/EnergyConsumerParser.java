package Converter.parsers;

import Converter.RDF;
import equip.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class EnergyConsumerParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +  "PREFIX dtps: <http://dtps.cloud/2023/schema-cim01#> "+
                "SELECT ?mRID ?name ?grounded ?pfixed ?p ?qfixed ?q ?ratedVoltage ?bvId ?vlId ?lrId " +
                "WHERE { " +
                " ?t a cim:EnergyConsumer ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:EnergyConsumer.grounded ?grounded ; " +
                " cim:EnergyConsumer.pfixed ?pfixed ; " +
                " cim:EnergyConsumer.p ?p ; " +
                " cim:EnergyConsumer.qfixed ?qfixed ; " +
                " cim:EnergyConsumer.q ?q ; " +
                " dtps:EnergyConsumer.ratedVoltage ?ratedVoltage ; " +
                " cim:ConductingEquipment.BaseVoltage ?bv ; " +
                " cim:Equipment.EquipmentContainer ?vl ;" +
                " cim:EnergyConsumer.LoadResponse ?lr." +
                " ?bv cim:IdentifiedObject.mRID ?bvId . " +
                " ?vl cim:IdentifiedObject.mRID ?vlId . " +
                " ?lr cim:IdentifiedObject.mRID ?lrId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                String grounded = solution.getValue("grounded").stringValue();
                String pfixed = solution.getValue("pfixed").stringValue();
                String p = solution.getValue("p").stringValue();
                String qfixed = solution.getValue("qfixed").stringValue();
                String q = solution.getValue("q").stringValue();
                String ratedVoltage = solution.getValue("ratedVoltage").stringValue();
                String baseVoltageId = solution.getValue("bvId").stringValue();
                String voltageLvlId= solution.getValue("vlId").stringValue();
                String loadResponseId= solution.getValue("lrId").stringValue();
                VoltageLevel voltageLevel = rdf.getVoltageLevels().stream().filter(t -> t.getMRID().equals(voltageLvlId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(t -> t.getMRID().equals(baseVoltageId)).findAny().get();
                LoadResponseCharacteristic loadResponseCharacteristic = rdf.getLoadResponseCharacteristics()
                        .stream().filter((t -> t.getMRID().equals(loadResponseId))).findAny().get();
                EnergyConsumer energyConsumer = new EnergyConsumer(mRID, name, Boolean.parseBoolean(grounded),
                        Double.parseDouble(pfixed), Double.parseDouble(p), Double.parseDouble(qfixed),
                        Double.parseDouble(q), Double.parseDouble(ratedVoltage), baseVoltage, voltageLevel,
                        loadResponseCharacteristic);

                System.out.println("energyConsumer");
                rdf.getEnergyConsumers().add(energyConsumer);
            }

        }
    }
}
