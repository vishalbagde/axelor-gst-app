package com.axelor.gst.sequence.test.standard;

import com.axelor.gst.db.service.SequenceService;
import com.axelor.test.GuiceModules;
import com.axelor.test.GuiceRunner;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceRunner.class)
@GuiceModules({MyModule.class})
public class TestSequence {

  @Inject public SequenceService sequenceService;

  @Test
  public void test() {
    // assertEquals("SALE0001SO", sequenceService.getSequence("Invoice"));
  }
}
