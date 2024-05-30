package Converter.parsers;

import Converter.RDF;
import equip.BaseVoltage;
import equip.PowerTransformer;
import equip.PowerTransformerEnd;
import equip.WindingConnection;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class PowerTransformerEndParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +  "PREFIX dtps: <http://dtps.cloud/2023/schema-cim01#> "+
                "SELECT ?mRID ?name ?endNumber ?phaseAngleClock ?grounded ?ratedU " +
                "?ratedS ?g ?b ?ex ?x ?r ?ck ?bvId ?ptId " +
                "WHERE { " +
                " ?t a cim:PowerTransformerEnd ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:TransformerEnd.endNumber ?endNumber ; " +
                " cim:PowerTransformerEnd.phaseAngleClock ?phaseAngleClock ; " +
                " cim:TransformerEnd.grounded ?grounded ; " +
                " cim:PowerTransformerEnd.ratedU ?ratedU ; " +
                " cim:PowerTransformerEnd.ratedS ?ratedS ; " +
                " cim:PowerTransformerEnd.g ?g ; " +
                " cim:PowerTransformerEnd.b ?b ; " +
                " dtps:PowerTransformerEnd.doesSaturationExist ?ex ; " +
                " cim:PowerTransformerEnd.x ?x ; " +
                " cim:PowerTransformerEnd.r ?r ; " +
                " cim:PowerTransformerEnd.connectionKind ?ck ; " +
                " cim:ConductingEquipment.BaseVoltage ?bv ; " +
                " cim:PowerTransformerEnd.PowerTransformer ?pt . " +
                " ?bv cim:IdentifiedObject.mRID ?bvId . " +
                " ?pt cim:IdentifiedObject.mRID ?ptId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                String endNumber = solution.getValue("endNumber").stringValue();
                String phaseAngleClock = solution.getValue("phaseAngleClock").stringValue();
                String grounded = solution.getValue("grounded").stringValue();
                String ratedU = solution.getValue("ratedU").stringValue();
                String ratedS = solution.getValue("ratedS").stringValue();
                String g = solution.getValue("g").stringValue();
                String b = solution.getValue("b").stringValue();
                String ex = solution.getValue("ex").stringValue();
                String x = solution.getValue("x").stringValue();
                String r = solution.getValue("r").stringValue();
                String ck = solution.getValue("ck").stringValue();

                String baseVoltageId = solution.getValue("bvId").stringValue();
                String powerTransformatorId= solution.getValue("ptId").stringValue();
                PowerTransformer powerTransformer = rdf.getPowerTransformers().stream().filter(t -> t.getMRID().equals(powerTransformatorId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(t -> t.getMRID().equals(baseVoltageId)).findAny().get();
                PowerTransformerEnd powerTransformerEnd = new PowerTransformerEnd(mRID, name, Integer.parseInt(endNumber),
                        Integer.parseInt(phaseAngleClock),Boolean.parseBoolean(grounded), Double.parseDouble(ratedU),
                        Double.parseDouble(ratedS), Double.parseDouble(g), Double.parseDouble(b),Boolean.parseBoolean(ex),
                        Double.parseDouble(x), Double.parseDouble(r), baseVoltage, powerTransformer,new WindingConnection(ck));
                System.out.println("powerTransformerEnd");
                rdf.getPowerTransformerEnds().add(powerTransformerEnd);
            }

        }
    }
}
