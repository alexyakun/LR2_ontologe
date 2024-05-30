package Converter.parsers;

import Converter.RDF;
import equip.BaseVoltage;
import equip.Substation;
import equip.VoltageLevel;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class VoltageLevelParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?bvId ?subId " +
                "WHERE { " +
                " ?t a cim:VoltageLevel ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:VoltageLevel.BaseVoltage ?bv ;" +
                " cim:VoltageLevel.Substation ?sub." +
                " ?sub cim:IdentifiedObject.mRID ?subId . " +
                " ?bv cim:IdentifiedObject.mRID ?bvId . " +

                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String baseVoltageId = solution.getValue("bvId").stringValue();
                String substationId = solution.getValue("subId").stringValue();
                Substation substation = rdf.getSubstations().stream().filter(x -> x.getMRID().equals(substationId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(x -> x.getMRID().equals(baseVoltageId)).findAny().get();
                VoltageLevel  voltageLevel = new VoltageLevel(baseVoltage, substation, mRID);
                rdf.getVoltageLevels().add(voltageLevel);
            }

        }

    }
}
