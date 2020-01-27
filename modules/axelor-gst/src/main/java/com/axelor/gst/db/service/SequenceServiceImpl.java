package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

  @Inject SequenceRepository sequenceRepo;

  @Transactional
  @Override
  public String getSequence(String model) {

    Sequence sequence =
        sequenceRepo.all().filter("self.model.name = :model").bind("model", model).fetchOne();

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
