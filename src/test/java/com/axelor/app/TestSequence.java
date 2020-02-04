package com.axelor.app;

import static org.junit.Assert.assertEquals;
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
  public void testAddress() {
    String seq = sequenceService.getSequence("Address");
    assertEquals("TEST0000000010", seq);
    System.out.println(seq);
  }
  @Test
  public void testInvoice() {
    String seq = sequenceService.getSequence("Invoice");
    assertEquals("SALE0005SO", seq);
    System.out.println(seq);
  }
  @Test
  public void testParty() {
    String seq = sequenceService.getSequence("Party");
    assertEquals("PRT0005NO", seq);
    System.out.println(seq);
  }
}