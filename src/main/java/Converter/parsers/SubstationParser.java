package Converter.parsers;

import Converter.RDF;
import equip.Substation;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class SubstationParser implements ElementParser
{
    @Override
    public  void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?name " +
                "WHERE { " +
                " ?t a cim:Substation ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                Substation substation = new Substation(mRID, name);
                rdf.getSubstations().add(substation);
            }
        }
    }
}
