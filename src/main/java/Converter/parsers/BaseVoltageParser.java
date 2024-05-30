package Converter.parsers;

import Converter.RDF;
import equip.BaseVoltage;
import lombok.NoArgsConstructor;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

@NoArgsConstructor
public class BaseVoltageParser implements ElementParser {
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?name ?nominalVoltage " +
                "WHERE { " +
                " ?t a cim:BaseVoltage ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name; " +
                " cim:BaseVoltage.nominalVoltage ?nominalVoltage; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                String nominalVoltage = solution.getValue("nominalVoltage").stringValue();
                BaseVoltage baseVoltage = new BaseVoltage(name, mRID,Double.parseDouble(nominalVoltage) );
                rdf.getBaseVoltages().add(baseVoltage);      }

        }

    }
}
