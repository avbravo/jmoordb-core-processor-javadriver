/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.avbravo.mongodbatlasdriver.repository;

import com.jmoordb.core.annotation.repository.Count;
import com.jmoordb.core.annotation.repository.CountRegex;
import com.jmoordb.core.annotation.Mandatory;
import com.jmoordb.core.annotation.repository.Ping;
import com.jmoordb.core.annotation.repository.Query;
import com.jmoordb.core.annotation.repository.QueryRegex;
import com.jmoordb.core.annotation.repository.Repository;
import com.jmoordb.core.annotation.enumerations.ActivatePagination;
import com.jmoordb.core.annotation.enumerations.ActivateSort;
import com.jmoordb.core.pagination.Pagination;
import com.avbravo.mongodbatlasdriver.model.Oceano;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import com.jmoordb.core.annotation.repository.QueryJSON;
import com.jmoordb.core.annotation.RepositoryMongoDB;
import com.jmoordb.core.annotation.enumerations.CaseSensitive;
import com.jmoordb.core.annotation.enumerations.JakartaSource;
import com.jmoordb.core.annotation.enumerations.TypeOrder;

/**
 *
 * @author avbravo
 */
@RepositoryMongoDB(entity = Oceano.class,jakartaSource = JakartaSource.JAVAEE_LEGACY)
public interface OceanoRepository {
//      @Mandatory
//    String getFirstName();
    @Query()
    public List<Oceano> findAll();

    @Query(where = "idoceano = @id")
    public Optional<Oceano> findById(String id);

    @Query(where = "oceano = @oceano")
    public List<Oceano> findByOceano(String oceano);

    @Query(where = "idoceano = @idoceano and oceano = @oceano")
    public List<Oceano> findByIdoceanoAndOceano(String idoceano, String oceano);

    @Query(where = "oceano = @oceano", activatePagination = ActivatePagination.ON, activateSort = ActivateSort.ON)
    public List<Oceano> findByOceanoPagination(String oceano, Pagination pagination, Document sort);

    @QueryJSON(activatePagination = ActivatePagination.ON, activateSort = ActivateSort.ON)
    public List<Oceano> queryJSON(Document filter, Pagination pagination, Document... sort);

    @QueryRegex(field = "oceano", activatePagination = ActivatePagination.ON, caseSensitive = CaseSensitive.NO, typeOrder = TypeOrder.ASC)
    public List<Oceano> findRegex(String value, Pagination pagination);

    @Count()
    public Integer count(Document... query);

    @CountRegex(field = "oceano", caseSensitive = CaseSensitive.NO)
    public Integer countRegex(String value);

    public Optional<Oceano> save(Oceano oceano);

    public Boolean update(Oceano oceano);

    public Boolean delete(String id);
    
    @Ping
    public Boolean ping();
}
