/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.avbravo.mongodbatlasdriver.supplier.services;

import com.jmoordb.core.annotation.Referenced;
import com.jmoordb.core.annotation.enumerations.TypePK;
import com.jmoordb.core.util.DocumentUtil;
import com.jmoordb.core.util.Test;
import com.avbravo.mongodbatlasdriver.model.Profesion;
import com.avbravo.mongodbatlasdriver.repository.ProfesionRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.bson.Document;

/**
 *
 * @author avbravo
 */
@RequestScoped
public class ProfesionSupplierServices implements Serializable {
    // <editor-fold defaultstate="collapsed" desc="@Inject">

    @Inject
    ProfesionRepository repository;

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Optional<Profesion> findByPK(Document document, Referenced referenced)">
    /**
     *
     * @param document
     * @param corregimientoReferenced
     * @return Devuelve un Optional del resultado de la busqueda por la llave
     * primaria Dependiendo si es entero o String
     */
    public Optional<Profesion> findByPK(Document document,Referenced referenced) {
        try {
            Optional<Profesion> optional = Optional.empty();
      if (referenced.typePK().equals(TypePK.STRING)) {
                optional = repository.findById(DocumentUtil.getIdValue(document, referenced));
            } else {
                //    profesionOptional  = profesionRepository.findById(Integer.parseInt(DocumentUtil.getIdValue(document, referenced)));
            }

            if (optional.isPresent()) {
                return optional;
            }

        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List<Profesion> findAllByPK(Document document, Referenced referenced) ">
    /**
     *
     * @param document
     * @param referenced
     * @return  List<Entity> en base a un @Referenced List<Entity> de la llave Primaria
     */
    public List<Profesion> findAllByPK(Document document, Referenced referenced) {
        List<Profesion> list = new ArrayList<>();
        try {
            List<Document> documentList = (List<Document>) document.get(referenced.from());
       
            List<Document> documentPkList = DocumentUtil.getListValue(document, referenced);
            if (documentPkList == null || documentPkList.isEmpty()) {
                Test.msg("No se pudo decomponer la lista de id referenced....");
            } else {
                for (Document documentPk : documentPkList) {
                    Optional<Profesion> optional = findByPK(documentPk, referenced);
                    if (optional.isPresent()) {
                        list.add(optional.get());
                    } else {
                        Test.warning("No tiene referencia a " + referenced.from());
                    }
                }
            }
        } catch (Exception e) {
            Test.error(Test.nameOfClassAndMethod() + " error() " + e.getLocalizedMessage());
        }
        return list;
    }
// </editor-fold>
}
