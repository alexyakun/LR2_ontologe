package Converter.parsers;

import Converter.RDF;
import equip.LoadResponseCharacteristic;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class LoadResponseCharacteristicParser implements ElementParser{
    @Override
    public void parse(RDF rdf, RepositoryConnection connection) {
        String queryString = "PREFIX cim: <" + cimUri + "> " +
                "SELECT ?mRID ?pVoltageExponent ?qVoltageExponent ?qConstantPower " +
                "WHERE { " +
                " ?t a cim:LoadResponseCharacteristic ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:LoadResponseCharacteristic.pVoltageExponent ?pVoltageExponent; " +
                " cim:LoadResponseCharacteristic.qVoltageExponent ?qVoltageExponent; " +
                " cim:LoadResponseCharacteristic.qConstantPower ?qConstantPower; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                String mRID = solution.getValue("mRID").stringValue();
                String pVoltageExponent = solution.getValue("pVoltageExponent").stringValue();
                String qVoltageExponent = solution.getValue("qVoltageExponent").stringValue();
                String qConstantPower = solution.getValue("qConstantPower").stringValue();
                LoadResponseCharacteristic loadResponseCharacteristic = new LoadResponseCharacteristic(mRID,
                        Double.parseDouble(pVoltageExponent),
                        Double.parseDouble(qVoltageExponent),
                        Double.parseDouble(qConstantPower));

                rdf.getLoadResponseCharacteristics().add(loadResponseCharacteristic);
            }
        }
    }
}
