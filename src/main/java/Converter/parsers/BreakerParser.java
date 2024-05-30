package Converter.parsers;

import Converter.RDF;
import equip.BaseVoltage;
import equip.Breaker;
import equip.VoltageLevel;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class BreakerParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?name ?bvId ?vlId ?open ?ratedCurrent " +
                "WHERE { " +
                " ?t a cim:Breaker ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:Switch.open ?open ; " +
                " cim:Switch.ratedCurrent ?ratedCurrent ; " +
                " cim:ConductingEquipment.BaseVoltage ?bv ; " +
                " cim:Equipment.EquipmentContainer ?vl." +
                " ?bv cim:IdentifiedObject.mRID ?bvId . " +
                " ?vl cim:IdentifiedObject.mRID ?vlId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                String baseVoltageId = solution.getValue("bvId").stringValue();
                String voltageLvlId= solution.getValue("vlId").stringValue();
                String open = solution.getValue("open").stringValue();
                String ratedCurrent = solution.getValue("ratedCurrent").stringValue();
                VoltageLevel voltageLevel = rdf.getVoltageLevels().stream().filter(t -> t.getMRID().equals(voltageLvlId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(t -> t.getMRID().equals(baseVoltageId)).findAny().get();
                Breaker breaker = new Breaker(mRID, name, Boolean.parseBoolean(open), Double.parseDouble(ratedCurrent), baseVoltage, voltageLevel);
                System.out.println("breaker");
                rdf.getBreakers().add(breaker);
            }

        }
    }
}
