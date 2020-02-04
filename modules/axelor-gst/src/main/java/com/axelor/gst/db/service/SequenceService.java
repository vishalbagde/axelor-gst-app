package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

  public String getSequence(String model);

  public String getInitialNextNumber(Sequence sequence);
}
