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

  @Override
  @Transactional
  public String getSequence(String model) {

    MetaModel m = metaModelRepo.findByName(model);

    Sequence sequence = sequenceRepo.findByModel(m);
    
    
    String seq = "";
    seq += sequence.getPrefix();
    seq += sequence.getNextNumber();
    String suf = sequence.getSuffix();
    /*
    if(suf=="") {
    }
    else {
    	seq += sequence.getSuffix();
    }
    */
    sequence.setNextNumber(Integer.parseInt(sequence.getNextNumber())+sequence.getPadding() + "");
    sequenceRepo.persist(sequence);
    return seq;
  }
}
