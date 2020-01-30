package TestEbicsOrderNumber;

import static org.junit.Assert.*;

import com.axelor.gst.db.service.SequenceService;
import com.axelor.gst.db.service.SequenceServiceImpl;
import org.junit.Test;

public class TestSequence {

  @Test
  public void test() {
    SequenceService seqser = new SequenceServiceImpl();
    assertEquals("SO-100-2020", seqser.getSequence("Invoice"));
  }
}
