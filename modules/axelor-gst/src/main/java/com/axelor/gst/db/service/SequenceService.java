package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

  public String getSequence(Sequence sequence, boolean isPersist);

}
