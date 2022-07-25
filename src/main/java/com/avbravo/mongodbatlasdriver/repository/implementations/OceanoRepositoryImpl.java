/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.mongodbatlasdriver.repository.implementations;

import com.jmoordb.core.annotation.enumerations.ActivatePagination;
import com.jmoordb.core.annotation.enumerations.ActivateSort;
import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.TypeOrder;
import com.jmoordb.core.pagination.Pagination;
import com.jmoordb.core.util.Test;
import com.avbravo.mongodbatlasdriver.model.Oceano;
import com.avbravo.mongodbatlasdriver.repository.OceanoRepository;
import com.avbravo.mongodbatlasdriver.supplier.OceanoSupplier;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
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
public class OceanoRepositoryImpl implements OceanoRepository {

    // <editor-fold defaultstate="collapsed" desc="@Inject">
    @Inject
    private Config config;
     @Inject
    MongoClient mongoClient;
     /**
      * Lee de la configuracion el nombre de la base de datos
      * que se especifica en @Repository(database={})
      */
// Lee de la configuracion el nombre de la base de datos
     
       @Inject
    @ConfigProperty(name = "mongodb.database")
       private String mongodbDatabase;
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Supplier">
    @Inject
    OceanoSupplier oceanoSupplier;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List<Oceano> findAll()">
    @Override
    public List<Oceano> findAll() {

        List<Oceano> list = new ArrayList<>();
        try {

            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);

            MongoCollection<Document> collection = database.getCollection("oceano");

            /**
             * Es una entidad de nivel 0 LookupSupplier.ZERO no usa lookup
             *
             */
            MongoCursor<Document> cursor = collection.find().iterator();

            try {
                while (cursor.hasNext()) {

                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Optional<Oceano> findById(String id) ">
    @Override
    public Optional<Oceano> findById(String id) {

        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");
            Document doc = collection.find(eq("idoceano", id)).first();

            Oceano oceano = oceanoSupplier.get(Oceano::new, doc);

            return Optional.of(oceano);
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Optional.empty();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Optional<Oceano> save(Oceano oceano)">

    @Override
    public Optional<Oceano> save(Oceano oceano) {
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");

            if (findById(oceano.getIdoceano()).isPresent()) {
                Test.warning("Eciste un registro con ese id");
                return Optional.empty();
            }

            Jsonb jsonb = JsonbBuilder.create();

            InsertOneResult insertOneResult = collection.insertOne(Document.parse(jsonb.toJson(oceano)));
            if (insertOneResult.getInsertedId() != null) {
                return Optional.of(oceano);
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Optional.empty();

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Boolean update(Oceano oceano) ">

    @Override
    public Boolean update(Oceano oceano) {
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");

            if (!findById(oceano.getIdoceano()).isPresent()) {
                Test.warning("Eciste un registro con ese id");
                return Boolean.FALSE;
            }
            Bson filter = Filters.empty();
            filter = Filters.eq("idoceano", oceano.getIdoceano());

            Jsonb jsonb = JsonbBuilder.create();

            UpdateResult result = collection.updateOne(filter, Document.parse(jsonb.toJson(oceano)));
            System.out.println("Matched document count: " + result.getMatchedCount());
            System.out.println("Modified document count: " + result.getModifiedCount());
            if (result.getModifiedCount() > 0) {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Boolean.FALSE;

    }
    // </editor-fold>  

    // <editor-fold defaultstate="collapsed" desc="Boolean delete(String id) "> 
    @Override
    public Boolean delete(String id) {
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");
            Bson filter = Filters.eq("idoceano", id);
            DeleteResult deleteResult = collection.deleteOne(filter);

            System.out.println("Modified document count: " + deleteResult.getDeletedCount());
            if (deleteResult.getDeletedCount() > 0) {
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return Boolean.FALSE;
    }
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="List<Oceano> findByOceano(String contry)">
    @Override
    public List<Oceano> findByOceano(String oceano) {
        List<Oceano> list = new ArrayList<>();
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");

            MongoCursor<Document> cursor = collection.find(eq("oceano", oceano)).iterator();

            try {
                while (cursor.hasNext()) {

                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="List<Oceano> queryJSON(Document filter, Pagination pagination, Document... sort)">

    /**
     *
     * @param filter
     * @param pagination
     * @param sort
     * @return Si pagination == null || pagination < 0 indica que no se usara
     * paginacion
     */
    @Override
    public List<Oceano> queryJSON(Document filter, Pagination pagination, Document... sort) {
        List<Oceano> list = new ArrayList<>();
        Document sortQuery = new Document();
        try {
            /**
             * Leer la anotacion @QueryJSON
             */

            ActivatePagination activatePagination = ActivatePagination.ON;
            ActivateSort activateSort = ActivateSort.ON;

            /**
             * DataBase
             */
            if (activateSort == ActivateSort.ON) {
                if (sort.length != 0) {
                    sortQuery = sort[0];
                }
            }
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");
            MongoCursor<Document> cursor;
            if (activatePagination == ActivatePagination.ON) {
                if (pagination == null || pagination.getPage() < 1) {
                    cursor = collection.find(filter).sort(sortQuery).iterator();
                } else {
                    cursor = collection.find(filter)
                            .skip(pagination.skip())
                            .limit(pagination.limit())
                            .sort(sortQuery).iterator();
                }

            } else {

                cursor = collection.find(filter)
                        .sort(sortQuery).iterator();
            }

            try {
                while (cursor.hasNext()) {
                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Integer count(Document... query">
    @Override
    public Integer count(Document... query) {
        Integer contador = 0;
        try {
            Document whereCondition = new Document();
            if (query.length != 0) {
                whereCondition = query[0];
            }
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");
            if (whereCondition.isEmpty()) {
                contador = (int) collection.countDocuments();
            } else {
                //  Document docQuery = DocumentUtil.jsonToDocument(whereCondition);
                contador = (int) collection.countDocuments(whereCondition);
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }
        return contador;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List<Oceano> findRegex(String query)">
    /**
     *
     * @param value
     * @param pagination
     * @return
     */
    @Override
    public List<Oceano> findRegex(String value, Pagination pagination) {
        List<Oceano> list = new ArrayList<>();
        try {
            /**
             * Leer la anotacion @QueryRegex field
             */
            String field = "oceano";
            ActivatePagination activatePagination = ActivatePagination.ON;
            CaseSensitive caseSensitive = CaseSensitive.NO;
            TypeOrder typeOrder = TypeOrder.ASC;
            /**
             * DataBase
             */
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");

            /**
             * Generar el patron
             */
            Pattern regex = Pattern.compile(value);
            /**
             * Generar ordenación 1. Se usa el atriiuto field de la anotacion
             *
             * @QueruRegex
             */
            // Genera ordenacion
            Integer order = 1;
            if (typeOrder == TypeOrder.DESC) {
                order = -1;
            }
            Document sort = new Document(field, order);
            /**
             * Se toma de la anotacion @QueryRegex campo caseSensitive con su
             * valor
             */

            MongoCursor<Document> cursor;

            /**
             * Verificar si usara pagainación o no. Verificar si s usara
             * caseSemsitive
             */
            if (activatePagination == ActivatePagination.OFF) {
                if (caseSensitive == CaseSensitive.NO) {
                    cursor = collection.find(new Document(field, new Document("$regex", "^" + value)))
                            .sort(sort)
                            .iterator();

                } else {
                    cursor = collection.find(new Document(field, new Document("$regex", "^" + value)
                            .append("$options", "i")))
                            .sort(sort)
                            .iterator();

                }
            } else {
                if (caseSensitive == CaseSensitive.NO) {
                    cursor = collection
                            .find(new Document(field, new Document("$regex", "^" + value)))
                            .skip(pagination.skip())
                            .limit(pagination.limit())
                            .sort(sort).iterator();

                } else {
                    cursor = collection
                            .find(new Document(field, new Document("$regex", "^" + value).append("$options", "i")))
                            .skip(pagination.skip())
                            .limit(pagination.limit())
                            .sort(sort).iterator();

                }
            }

            try {
                while (cursor.hasNext()) {

                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Integer countRegex(String value)">
    /**
     *
     * @param value
     * @return total de documentos que cumolen con la condicion Regex
     */
    @Override
    public Integer countRegex(String value) {
        Integer contador = 0;
        try {
            /**
             * Leer la anotacion @CountRegex
             */
            String field = "oceano";
            CaseSensitive caseSensitive = CaseSensitive.NO;

            /**
             * Leer la base de datos
             */
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);

            MongoCollection<Document> collection = database.getCollection("oceano");

            /**
             * Generar el patron
             */
            Pattern regex = Pattern.compile(value);

            MongoCursor<Document> cursor;

            /**
             * Verificar si usara pagainación o no. Verificar si s usara
             * caseSemsitive
             */
            if (caseSensitive == CaseSensitive.NO) {
                contador = (int) collection.countDocuments(new Document("oceano", new Document("$regex", "^" + value)));

            } else {
                contador = (int) collection.countDocuments(new Document("oceano", new Document("$regex", "^" + value).append("$options", "i")));

            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return contador;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List<Oceano> findByOceanoPagination(String oceano, Pagination pagination) ">
    /**
     *
     * @param oceano
     * @param pagination
     * @return List<Entity> de la consulta aplicando paginación
     */
    @Override
    public List<Oceano> findByOceanoPagination(String oceano, Pagination pagination, Document sort) {
        List<Oceano> list = new ArrayList<>();
        try {
            /**
             * Leer la anotacion @QueryRegex field
             */
            String field = "oceano";
            ActivatePagination activatePagination = ActivatePagination.ON;
            CaseSensitive caseSensitive = CaseSensitive.NO;
            TypeOrder typeOrder = TypeOrder.ASC;

            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");

            /**
             * Lee de la anotación @Query la propiedad activatePagination =
             * ActivatePagination.ON o ActivatePagination.OFF
             */
            MongoCursor<Document> cursor;
            if (activatePagination == ActivatePagination.ON) {
                if (pagination == null || pagination.getPage() < 0) {
                    Test.msg("Paginación no es valida");
                    return new ArrayList<>();
                } else {
                    cursor = collection.find(eq("oceano", oceano))
                            .skip(pagination.skip())
                            .limit(pagination.limit())
                            .sort(sort)
                            .iterator();
                }

            } else {
                cursor = collection.find(eq("oceano", oceano))
                        .sort(sort)
                        .iterator();
            }

            try {
                while (cursor.hasNext()) {

                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List<Oceano> findByIdoceanoAndOceano(String idoceano, String oceano)">
    @Override
    public List<Oceano> findByIdoceanoAndOceano(String idoceano, String oceano) {
        List<Oceano> list = new ArrayList<>();
        Document sortQuery = new Document();
        try {
            /**
             * Leer la anotacion @Query
             */

            Document filter = new Document("idoceano", idoceano).append("oceano", oceano);

            /**
             * DataBase
             */
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);
            MongoCollection<Document> collection = database.getCollection("oceano");
            MongoCursor<Document> cursor;

            cursor = collection.find(filter)
                    .sort(sortQuery).iterator();

            try {
                while (cursor.hasNext()) {
                    list.add(oceanoSupplier.get(Oceano::new, cursor.next()));
                }
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return list;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Boolean ping()">
    @Override
    public Boolean ping() {
        Boolean conected = Boolean.FALSE;
        try {
            MongoDatabase database = mongoClient.getDatabase(mongodbDatabase);

            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                Document commandResult = database.runCommand(command);
                System.out.println("Connected successfully to server.");
                conected = Boolean.TRUE;
            } catch (MongoException me) {
                System.err.println("An error occurred while attempting to run a command: " + me);
                Test.error(Test.nameOfClassAndMethod() + " " + me.getLocalizedMessage());

            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " " + e.getLocalizedMessage());
        }

        return conected;
    }
    // </editor-fold>
}
