/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.mongodbatlasdriver.repository.implementations;

import com.jmoordb.core.util.MessagesUtil;
import com.avbravo.mongodbatlasdriver.repository.AutogeneratedRepository;
import com.jmoordb.core.model.Autosequence;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.InsertOneResult;
import java.util.Optional;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.bson.Document;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author avbravo
 */
@ApplicationScoped
public class AutogeneratedRepositoryImpl implements AutogeneratedRepository {

    // <editor-fold defaultstate="collapsed" desc="@Inject">
    @Inject
    private Config config;
    @Inject
    @ConfigProperty(name = "mongodb.database")
    private String mongodbDatabase;
    private String mongodbCollection = "autosequence";
    @Inject
    MongoClient mongoClient;
 

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Supplier">
    
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="public Integer generate(String database, String collection)">
    @Override
    public Integer generate(String database, String collection) {


        try {

            Optional<Autosequence> autosequenceOptional = findById(database + "_" + collection);
            if (!autosequenceOptional.isPresent()) {
                Autosequence autosequence = new Autosequence(database + "_" + collection, 0);
                save(autosequence);
            }

            Optional<Autosequence> autosequenceIncrementOptional = findOneAndUpdate(database + "_" + collection);

            if (!autosequenceIncrementOptional.isPresent()) {
                return -1;
            } else {
                return autosequenceIncrementOptional.get().getSecuence();
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return -1;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Optional<Autosequence> findById(String databasecollection)">
    public Optional<Autosequence> findById(String databasecollection) {

        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection(mongodbCollection);
            Document doc = collection.find(eq("databasecollection", databasecollection)).first();

            Autosequence autosequence = get(Autosequence::new, doc);

            return Optional.of(autosequence);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Optional.empty();
    }
    // </editor-fold>

  
    // <editor-fold defaultstate="collapsed" desc="Optional<Autosequence> save(Autosequence autosequence)">

    public Optional<Autosequence> save(Autosequence autosequence) {
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection(mongodbCollection);

            Jsonb jsonb = JsonbBuilder.create();

            InsertOneResult insertOneResult = collection.insertOne(Document.parse(jsonb.toJson(autosequence)));
            if (insertOneResult.getInsertedId() != null) {
                return Optional.of(autosequence);
            }

        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Optional.empty();

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Optional<Autosequence> findOneAndUpdate(String databasecollection) ">
/**
 * Realiza el incremento.
 * @param databasecollection
 * @return 
 */
    public Optional<Autosequence> findOneAndUpdate(String databasecollection) {

        try {
            Integer increment = 1;

            Document doc = new Document("databasecollection", databasecollection);
            Document inc = new Document("$inc", new Document("sequence", increment));

            FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();
            findOneAndUpdateOptions.upsert(true);

            findOneAndUpdateOptions.returnDocument(ReturnDocument.AFTER);

            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection(mongodbCollection);
            Document iterable = collection.findOneAndUpdate(doc, inc, findOneAndUpdateOptions);
            Autosequence autosequence = get(Autosequence::new, iterable);
            return Optional.of(autosequence);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Optional.empty();
    }// </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Autosequence get(Supplier<? extends Autosequence> s, Document document) ">
    public Autosequence get(Supplier<? extends Autosequence> s, Document document) {

        Autosequence autosequence = s.get();
        try {

              Jsonb jsonb = JsonbBuilder.create();
            autosequence = jsonb.fromJson(document.toJson(), Autosequence.class);
        } catch (Exception e) {
            MessagesUtil.error(MessagesUtil.nameOfClassAndMethod() + " " + e.getLocalizedMessage());

        }
        return autosequence;

    }
// </editor-fold>
}
