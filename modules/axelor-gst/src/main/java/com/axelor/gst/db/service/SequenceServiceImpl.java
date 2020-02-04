package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;

public class SequenceServiceImpl implements SequenceService {

  @Inject SequenceRepository sequenceRepo;

  @Transactional
  @Override
  public String getSequence(String model) {
    Sequence sequence =
        sequenceRepo.all().filter("self.model.name = :model").bind("model", model).fetchOne();
    if (sequence != null) {
      String seq = "";
      if (sequence.getNextNumber() == null) {
        seq = getInitialNextNumber(sequence);
        return seq;
      } else {
        seq = sequence.getNextNumber();
        String nextNumber = sequence.getNextNumber();

        if (sequence.getPrefix() != null) nextNumber = nextNumber.replace(sequence.getPrefix(), "");

        if (sequence.getSuffix() != null) nextNumber = nextNumber.replace(sequence.getSuffix(), "");

        String nextSeq = "";
        if (StringUtils.isNumeric(nextNumber)) {
          nextNumber = Integer.parseInt(nextNumber) + 1 + "";
          nextSeq += sequence.getPrefix();
          nextSeq += StringUtils.leftPad(nextNumber + "", sequence.getPadding(), "0");
          if (sequence.getSuffix() != null) {
            nextSeq += sequence.getSuffix();
          }
        } else {
          nextSeq = getInitialNextNumber(sequence);
        }
        sequence.setNextNumber(nextSeq);
        sequenceRepo.persist(sequence);
        return seq;
      }
    } else {
      return null;
    }
  }

  @Override
  public String getInitialNextNumber(Sequence sequence) {
    String seq = "";
    int nextNumber = 1;
    seq += sequence.getPrefix();
    seq += StringUtils.leftPad(nextNumber + "", sequence.getPadding(), "0");
    if (sequence.getSuffix() != null) {
      seq += sequence.getSuffix();
    }
    return seq;
  }
}
