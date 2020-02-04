package com.axelor.app;

import static org.junit.Assert.assertEquals;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.test.GuiceModules;
import com.axelor.test.GuiceRunner;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GuiceRunner.class)
@GuiceModules({MyModule.class})
public class TestSequence {

  @Inject public SequenceService sequenceService;
  @Inject public SequenceRepository sequenceRepo;

  Sequence sequence;

  @Before
  public void initial() {
    sequence =
        sequenceRepo
            .all()
            .filter("self.model.name = :model")
            .bind("model", Invoice.class.getSimpleName())
            .fetchOne();
  }

  @Test
  public void testAddress() {

    String seq = sequenceService.getSequence(sequence, true);
    assertEquals("TEST0000000010", seq);
    System.out.println(seq);
  }

  @Test
  public void testInvoice() {
    String seq = sequenceService.getSequence(sequence, true);
    assertEquals("SALE0005SO", seq);
    System.out.println(seq);
  }

  @Test
  public void testParty() {
    String seq = sequenceService.getSequence(sequence, true);
    assertEquals("PRT0005NO", seq);
    System.out.println(seq);
  }
}
