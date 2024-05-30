package Converter.parsers;

import Converter.RDF;
import equip.BaseVoltage;
import equip.EquivalentInjection;
import equip.VoltageLevel;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class EquivalentInjectionParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?name ?bvId ?vlId ?r ?r2 ?x ?x2 ?r0 ?x0 " +
                "WHERE { " +
                " ?t a cim:EquivalentInjection ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:EquivalentInjection.r ?r ; " +
                " cim:EquivalentInjection.r2 ?r2 ; " +
                " cim:EquivalentInjection.x ?x ; " +
                " cim:EquivalentInjection.x2 ?x2 ; " +
                " cim:EquivalentInjection.r0 ?r0 ; " +
                " cim:EquivalentInjection.x0 ?x0 ; " +
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
                String r = solution.getValue("r").stringValue();
                String r2 = solution.getValue("r2").stringValue();
                String x = solution.getValue("x").stringValue();
                String x2 = solution.getValue("x2").stringValue();
                String r0 = solution.getValue("r0").stringValue();
                String x0 = solution.getValue("x0").stringValue();
                VoltageLevel voltageLevel = rdf.getVoltageLevels().stream().filter(t -> t.getMRID().equals(voltageLvlId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(t -> t.getMRID().equals(baseVoltageId)).findAny().get();
                EquivalentInjection equivalentInjection = new EquivalentInjection(mRID, name, baseVoltage, voltageLevel,
                        Double.parseDouble(r), Double.parseDouble(r2), Double.parseDouble(x),Double.parseDouble(x2),
                        Double.parseDouble(r0),Double.parseDouble(x0));
                System.out.println("voltageLevel");
                rdf.getEquivalentInjections().add(equivalentInjection);
            }

        }
    }
}
