package Converter.parsers;

import Converter.RDF;
import equip.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class ACLineSegmentParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " + "PREFIX dtps: <http://dtps.cloud/2023/schema-cim01#> "+
                "SELECT ?mRID ?name ?bvId ?lineId ?length ?r ?r0 ?x ?x0 ?bch ?b0ch ?rap ?ucp " +
                "WHERE { " +
                " ?t a cim:ACLineSegment ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:Conductor.length ?length ; " +
                " cim:ACLineSegment.r ?r ; " +
                " cim:ACLineSegment.r0 ?r0 ; " +
                " cim:ACLineSegment.x ?x ; " +
                " cim:ACLineSegment.x0 ?x0 ; " +
                " cim:ACLineSegment.bch ?bch ; " +
                " cim:ACLineSegment.b0ch ?b0ch ; " +
                " dtps:ACLineSegment.ratedActivePower ?rap ; " +
                " dtps:ACLineSegment.userConcentratedParameters ?ucp ; " +
                " cim:ConductingEquipment.BaseVoltage ?bv ; " +
                " cim:Equipment.EquipmentContainer ?vl." +
                " ?bv cim:IdentifiedObject.mRID ?bvId . " +
                " ?vl cim:IdentifiedObject.mRID ?lineId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                String length = solution.getValue("length").stringValue();
                String baseVoltageId = solution.getValue("bvId").stringValue();
                String lineId= solution.getValue("lineId").stringValue();
                String r = solution.getValue("r").stringValue();
                String r0 = solution.getValue("r0").stringValue();
                String x = solution.getValue("x").stringValue();
                String x0 = solution.getValue("x0").stringValue();
                String bch = solution.getValue("bch").stringValue();
                String b0ch = solution.getValue("b0ch").stringValue();
                String rap = solution.getValue("rap").stringValue();
                String ucp = solution.getValue("ucp").stringValue();
                Line line = rdf.getLines().stream().filter(t -> t.getMRID().equals(lineId)).findAny().get();
                BaseVoltage baseVoltage = rdf.getBaseVoltages().stream().filter(t -> t.getMRID().equals(baseVoltageId)).findAny().get();
                ACLineSegment acLineSegment = new ACLineSegment(mRID, name, Double.parseDouble(length),Double.parseDouble(r),
                        Double.parseDouble(r0),Double.parseDouble(x), Double.parseDouble(x0),
                        Double.parseDouble(bch),Double.parseDouble(b0ch), Double.parseDouble(rap), Boolean.parseBoolean(ucp),
                        baseVoltage, line);
                System.out.println("acLine");
                rdf.getAcLineSegments().add(acLineSegment);
            }

        }
    }
}
