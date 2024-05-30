import Converter.CimToJavaConverter;
import lombok.SneakyThrows;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Tests {

    @SneakyThrows
    @Test
    public void tester(){

        File initialFile = new File("src/main/resources/Substation_220_10.xml");
        InputStream targetStream = new FileInputStream(initialFile);

        Model model = Rio.parse(targetStream,"http://iec.ch/TC57/2013/CIM-schema-cim16#", RDFFormat.RDFXML);
        CimToJavaConverter cimToJavaConverter = new CimToJavaConverter();
        cimToJavaConverter.converterCimToJava(model);
    }
}
