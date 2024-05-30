package Converter.parsers;

import Converter.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public interface ElementParser {
    String cimUri = "http://iec.ch/TC57/2016/CIM-schema-cim#";
    public void parse(RDF rdf, RepositoryConnection connection);
}
