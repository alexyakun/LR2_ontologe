package Converter.parsers;

import Converter.RDF;
import equip.BaseFrequency;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class BaseFrequencyParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?frequency " +
                "WHERE { " +
                " ?t a cim:BaseFrequency ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:BaseFrequency.frequency ?frequency; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String frequency = solution.getValue("frequency").stringValue();
                BaseFrequency baseFrequency = new BaseFrequency();
                baseFrequency.setMRID(mRID);
                baseFrequency.setFrequency(Double.parseDouble(frequency));
                rdf.getBaseFrequency().add(baseFrequency);
            }

        }
    }
}
