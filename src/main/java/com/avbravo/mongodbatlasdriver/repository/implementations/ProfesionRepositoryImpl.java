/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.mongodbatlasdriver.repository.implementations;

import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.util.Test;
import com.avbravo.mongodbatlasdriver.model.Profesion;
import com.avbravo.mongodbatlasdriver.repository.ProfesionRepository;
import com.avbravo.mongodbatlasdriver.supplier.ProfesionSupplier;
import com.avbravo.mongodbatlasdriver.supplier.ProvinciaSupplier;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author avbravo
 */
@ApplicationScoped
//@Stateless
public class ProfesionRepositoryImpl implements ProfesionRepository {

    // <editor-fold defaultstate="collapsed" desc="@Inject">

    @Inject
    private Config config;
     @Inject
    @ConfigProperty(name = "mongodb.database")
       private String mongodbDatabase;
    @Inject
    MongoClient mongoClient;
// </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Supplier">
    @Inject
    ProfesionSupplier profesionSupplier;
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List<Profesion> findAll()">

    @Override
    public List<Profesion> findAll() {

      List<Profesion> list = new ArrayList<>();
        try {
        
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("profesion");
            
              MongoCursor<Document> cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                       list.add(profesionSupplier.get(Profesion::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " "+e.getLocalizedMessage());
        }
        return list;
    }
// </editor-fold>
    @Override
    public Optional<Profesion> findById(String id) {

        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("profesion");
            Document doc = collection.find(eq("idprofesion", id)).first();
           
            Profesion profesion = profesionSupplier.get(Profesion::new,doc);

            return Optional.of(profesion);
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " "+e.getLocalizedMessage());
        }

        return Optional.empty();
    }

    @Override
    public Profesion save(Profesion profesion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean deleteById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Profesion> findByProfesion(String contry) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
