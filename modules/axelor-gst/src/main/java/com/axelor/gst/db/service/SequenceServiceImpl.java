package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

  @Inject SequenceRepository sequenceRepo;
  @Inject MetaModelRepository metaModelRepo;

  @Transactional
  @Override
  public String getSequence(String model) {

    // Sequence sequence =sequenceRepo.all().filter("self.model= :model").bind("model",
    // model).fetchOne();
    /*Query.of(Sequence.class)
    .filter("self.model = :model")
    .bind("model", model)
    .fetchOne();
    */
    // Sequence sequence = sequenceRepo.all().filter("self.model = :model").bind("model",
    // model).fetchOne();
    MetaModel metamodel = metaModelRepo.findByName(model);
    Sequence sequence = sequenceRepo.findByModel(metamodel);
    if (sequence != null) {
      String seq = "";
      seq += sequence.getPrefix();
      seq += sequence.getNextNumber();
      if (sequence.getSuffix() != null) {
        seq += sequence.getSuffix();
      }
      sequence.setNextNumber(
          Integer.parseInt(sequence.getNextNumber()) + sequence.getPadding() + "");
      sequenceRepo.persist(sequence);
      return seq;
    } else {
      return null;
    }
  }
}
