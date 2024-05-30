package Converter;

import equip.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.File;
import java.io.IOException;

public class CimToJavaConverter {

    AllObjects objects = new AllObjects();

    @SneakyThrows
    public void converterCimToJava(Model model) {

        Repository repository = new SailRepository(new MemoryStore());
        RepositoryConnection connection = repository.getConnection();
        connection.add(model);
        getSubstation(connection);
        getBaseVoltage(connection);
        getVoltageLevel(connection);
        getPowerTransformer(connection);
        //getEquivalentInjection(connection);
        getPowerTransformerEnd(connection);
        getACLineSegment(connection);
        getConformLoad(connection);
        WriteClassesToJson();
    }

    public void getSubstation(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?sId ?name " +
                "WHERE { " +
                " ?t a cim:Substation ; " +
                " cim:IdentifiedObject.mRID ?sId ; " +
                " cim:IdentifiedObject.name ?name; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String name = solution.getValue("name").stringValue();
                String sId = solution.getValue("sId").stringValue();

                Substation substation  = new Substation(name, sId);
                objects.getSubstation().add(substation);
            }
        }
    }

    public void getBaseVoltage(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?name ?nomU " +
                "WHERE { " +
                " ?t a cim:BaseVoltage ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:BaseVoltage.nominalVoltage ?nomU ; " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String name = solution.getValue("name").stringValue();
                String mRID = solution.getValue("mRID").stringValue();
                Voltage nomU = new Voltage();
                nomU.setValue(Float.parseFloat(solution.getValue("nomU").stringValue()));

                //BaseVoltage baseVoltage = new BaseVoltage(name, mRID, nomU);
                //objects.getBaseVoltages().add(baseVoltage);
            }
        }
    }

    public void getVoltageLevel(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?subId ?baseUId " +
                "WHERE { " +
                " ?t a cim:VoltageLevel ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:VoltageLevel.Substation ?sub ; " +
                " cim:VoltageLevel.BaseVoltage ?baseU . " +
                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
                " ?sub cim:IdentifiedObject.mRID ?subId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);


        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String mRID = solution.getValue("mRID").stringValue();
                BaseVoltage baseU = new BaseVoltage();
                for ( BaseVoltage el : objects.getBaseVoltages()){
                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
                }
                Substation sub = new Substation();
                for ( Substation el : objects.getSubstation()){
                    if (el.getMRID().equals(solution.getValue("subId").stringValue())) sub = el;
                }

                VoltageLevel voltageLevel = new VoltageLevel(baseU, sub, mRID);
                objects.getVoltageLevels().add(voltageLevel);
            }
        }
    }

//    public void getEquivalentInjection(RepositoryConnection connection) {
//
//        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
//                "SELECT ?mRID ?name ?baseUId ?maxQ ?minQ ?p ?q ?r ?r0 ?r2 ?x ?x0 ?x2 " +
//                "WHERE { " +
//                " ?t a cim:EquivalentInjection ; " +
//                " cim:IdentifiedObject.mRID ?mRID ; " +
//                " cim:IdentifiedObject.name ?name ; " +
//                " cim:EquivalentInjection.p ?p ; " +
//                " cim:EquivalentInjection.q ?q ; " +
//                " cim:EquivalentInjection.r ?r ; " +
//                " cim:EquivalentInjection.r0 ?r0 ; " +
//                " cim:EquivalentInjection.r2 ?r2 ; " +
//                " cim:EquivalentInjection.x ?x ; " +
//                " cim:EquivalentInjection.x0 ?x0 ; " +
//                " cim:EquivalentInjection.x2 ?x2 ; " +
//                " cim:ConductingEquipment.BaseVoltage ?baseU . " +
//                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
//                " OPTIONAL { ?t cim:EquivalentInjection.maxQ ?maxQ .} " +
//                " OPTIONAL { ?t cim:EquivalentInjection.minQ ?minQ .} " +
//                "}";
//        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//
//        try (TupleQueryResult result = query.evaluate()) {
//            for (BindingSet solution : result) {
//                EquivalentInjection equivalentInjection;
//
//                String mRID = solution.getValue("mRID").stringValue();
//                String name = solution.getValue("name").stringValue();
//                Resistance r = new Resistance();
//                r.setValue(Float.parseFloat(solution.getValue("r").stringValue()));
//                Resistance r0 = new Resistance();
//                r0.setValue(Float.parseFloat(solution.getValue("r0").stringValue()));
//                Resistance r2 = new Resistance();
//                r2.setValue(Float.parseFloat(solution.getValue("r2").stringValue()));
//                Resistance x = new Resistance();
//                x.setValue(Float.parseFloat(solution.getValue("x").stringValue()));
//                Resistance x0 = new Resistance();
//                x0.setValue(Float.parseFloat(solution.getValue("x0").stringValue()));
//                Resistance x2 = new Resistance();
//                x2.setValue(Float.parseFloat(solution.getValue("x2").stringValue()));
//                ReactivePower q = new ReactivePower();
//                q.setValue(Float.parseFloat(solution.getValue("q").stringValue()));
//                ActivePower p = new ActivePower();
//                p.setValue(Float.parseFloat(solution.getValue("p").stringValue()));
//                BaseVoltage baseU = new BaseVoltage();
//                for ( BaseVoltage el : objects.getBaseVoltages()){
//                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
//                }
//                if ( solution.getValue("maxQ") != null){
//                    ReactivePower maxQ = new ReactivePower();
//                    maxQ.setValue(Float.parseFloat(solution.getValue("maxQ").stringValue()));
//                    ReactivePower minQ = new ReactivePower();
//                    minQ.setValue(Float.parseFloat(solution.getValue("minQ").stringValue()));
//                    equivalentInjection = new EquivalentInjection(
//                            mRID, name, baseU,  p, q, r, r0, r2, x, x0, x2);
//                }else {equivalentInjection = new EquivalentInjection(
//                        mRID, name, baseU, p, q, r, r0, r2, x, x0, x2);}
//                objects.getEquivalentInjections().add(equivalentInjection);
//            }
//        }
//    }

    public void getPowerTransformerEnd(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?endNum ?baseUId ?trasfId ?ck ?ratedU ?ratedS ?r ?r0 ?x ?x0 ?b ?b0 " +
                "WHERE { " +
                " ?t a cim:PowerTransformerEnd ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:PowerTransformerEnd.ratedU ?ratedU ; " +
                " cim:TransformerEnd.endNumber ?endNum ; " +
                " cim:PowerTransformerEnd.connectionKind ?ck ; " +
                " cim:PowerTransformerEnd.r ?r ; " +
                " cim:PowerTransformerEnd.r0 ?r0 ; " +
                " cim:PowerTransformerEnd.x ?x ; " +
                " cim:PowerTransformerEnd.x0 ?x0 ; " +
                " cim:PowerTransformerEnd.b ?b ; " +
                " cim:PowerTransformerEnd.b0 ?b0 ; " +
                " cim:PowerTransformerEnd.PowerTransformer ?pt ; " +
                " cim:TransformerEnd.BaseVoltage ?baseU . " +
                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
                " ?pt cim:IdentifiedObject.mRID ?trasfId  . " +
                " OPTIONAL { ?t cim:PowerTransformerEnd.ratedS ?ratedS .} " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {
                PowerTransformerEnd powerTransformerEnd;

                String mRID = solution.getValue("mRID").stringValue();
                WindingConnection ck = new WindingConnection();
                ck.setValue(solution.getValue("ck").stringValue());
                Resistance r = new Resistance();
                r.setValue(Float.parseFloat(solution.getValue("r").stringValue()));
                Resistance r0 = new Resistance();
                r0.setValue(Float.parseFloat(solution.getValue("r0").stringValue()));
                Resistance x = new Resistance();
                x.setValue(Float.parseFloat(solution.getValue("x").stringValue()));
                Resistance x0 = new Resistance();
                x0.setValue(Float.parseFloat(solution.getValue("x0").stringValue()));
                Susceptance b = new Susceptance();
                b.setValue(Float.parseFloat(solution.getValue("b").stringValue()));
                Susceptance b0 = new Susceptance();
                b0.setValue(Float.parseFloat(solution.getValue("b0").stringValue()));
                Voltage ratedU = new Voltage();
                ratedU.setValue(Float.parseFloat(solution.getValue("ratedU").stringValue()));
                int endNum = Integer.parseInt(solution.getValue("endNum").stringValue());
                BaseVoltage baseU = new BaseVoltage();
                for ( BaseVoltage el : objects.getBaseVoltages()){
                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
                }
                PowerTransformer powerTransformer = new PowerTransformer();
                for ( PowerTransformer el : objects.getPowerTransformers()){
                    if (el.getMRID().equals(solution.getValue("trasfId").stringValue())) powerTransformer = el;
                }
//                if ( solution.getValue("ratedS") != null){
//                    ApparentPower ratedS = new ApparentPower();
//                    ratedS.setValue(Float.parseFloat(solution.getValue("ratedS").stringValue()));
//                    powerTransformerEnd = new PowerTransformerEnd(
//                            mRID, baseU, powerTransformer, endNum, ck, ratedU, ratedS, b, b0, r, r0, x, x0);
//                }else {powerTransformerEnd = new PowerTransformerEnd(
//                        mRID,powerTransformer, baseU, endNum, ck, ratedU, r, r0, x, x0,b, b0);}
//                objects.getPowerTransformerEnds().add(powerTransformerEnd);
            }
        }
    }

    public void getACLineSegment(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?name ?baseUId ?length ?r ?r0 ?x ?x0 ?bch ?b0ch " +
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
                " cim:ConductingEquipment.BaseVoltage ?baseU . " +
                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                Length length = new Length();
                length.setValue(Float.parseFloat(solution.getValue("length").stringValue()));
                Resistance r = new Resistance();
                r.setValue(Float.parseFloat(solution.getValue("r").stringValue()));
                Resistance r0 = new Resistance();
                r0.setValue(Float.parseFloat(solution.getValue("r0").stringValue()));
                Resistance x = new Resistance();
                x.setValue(Float.parseFloat(solution.getValue("x").stringValue()));
                Resistance x0 = new Resistance();
                x0.setValue(Float.parseFloat(solution.getValue("x0").stringValue()));
                Susceptance bch = new Susceptance();
                bch.setValue(Float.parseFloat(solution.getValue("bch").stringValue()));
                Susceptance b0ch = new Susceptance();
                b0ch.setValue(Float.parseFloat(solution.getValue("b0ch").stringValue()));
                BaseVoltage baseU = new BaseVoltage();
                for ( BaseVoltage el : objects.getBaseVoltages()){
                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
                }
//                ACLineSegment acLineSegment = new ACLineSegment(baseU, length, mRID, name, b0ch, bch, r, r0, x, x0);
//                objects.getAcLineSegments().add(acLineSegment);
            }
        }
    }

    public void getConformLoad(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?name ?baseUId ?vlId ?pfixed ?qfixed " +
                "WHERE { " +
                " ?t a cim:ConformLoad ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:EnergyConsumer.pfixed ?pfixed ; " +
                " cim:EnergyConsumer.qfixed ?qfixed ; " +
                " cim:Equipment.EquipmentContainer ?ec ; " +
                " cim:ConductingEquipment.BaseVoltage . " +
                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
                " ?ec cim:IdentifiedObject.mRID ?vlId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);


        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                Length length = new Length();
                length.setValue(Float.parseFloat(solution.getValue("length").stringValue()));
                ReactivePower qfixed = new ReactivePower();
                qfixed.setValue(Float.parseFloat(solution.getValue("qfixed").stringValue()));
                ActivePower pfixed = new ActivePower();
                pfixed.setValue(Float.parseFloat(solution.getValue("pfixed").stringValue()));
                BaseVoltage baseU = new BaseVoltage();
                for ( BaseVoltage el : objects.getBaseVoltages()){
                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
                }
                EquipmentContainer ec = new EquipmentContainer();
                for ( VoltageLevel el : objects.getVoltageLevels()){
                    if (el.getMRID().equals(solution.getValue("vlId").stringValue())) ec.setVoltageLevel(el);
                }

                ConformLoad conformLoad = new ConformLoad(mRID, name, baseU, ec, pfixed, qfixed);
                objects.getConformLoads().add(conformLoad);
            }
        }
    }


    public void getPowerTransformer(RepositoryConnection connection) {

        String queryString = "PREFIX cim: <" + "http://iec.ch/TC57/2013/CIM-schema-cim16#" + "> " +
                "SELECT ?mRID ?name ?baseUId ?vlId " +
                "WHERE { " +
                " ?t a cim:PowerTransformer ; " +
                " cim:IdentifiedObject.mRID ?mRID ; " +
                " cim:IdentifiedObject.name ?name ; " +
                " cim:ConductingEquipment.BaseVoltage ?baseU ; " +
                " cim:Equipment.EquipmentContainer ?vl . " +
                " ?baseU cim:IdentifiedObject.mRID ?baseUId  . " +
                " ?vl cim:IdentifiedObject.mRID ?vlId . " +
                "}";
        TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);


        try (TupleQueryResult result = query.evaluate()) {
            for (BindingSet solution : result) {

                String mRID = solution.getValue("mRID").stringValue();
                String name = solution.getValue("name").stringValue();
                BaseVoltage baseU = new BaseVoltage();
                for ( BaseVoltage el : objects.getBaseVoltages()){
                    if (el.getMRID().equals(solution.getValue("baseUId").stringValue())) baseU = el;
                }
                EquipmentContainer ec = new EquipmentContainer();
                for ( VoltageLevel el : objects.getVoltageLevels()){
                    if (el.getMRID().equals(solution.getValue("vlId").stringValue())) ec.setVoltageLevel(el);
                }

//                PowerTransformer powerTransformer = new PowerTransformer(mRID, name, baseU, ec);
//                objects.getPowerTransformers().add(powerTransformer);
            }
        }
    }


    public  void WriteClassesToJson() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(
                new File("src/main/resources/jsonModel1.json")
                ,objects);
        System.out.println();
    }
}