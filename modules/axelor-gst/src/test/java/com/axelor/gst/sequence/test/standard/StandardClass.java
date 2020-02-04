package com.axelor.gst.sequence.test.standard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StandardClass {

  @Before
  public void displatBefore() {
    System.out.println("Before Annotation");
  }

  @Test
  public void test() {
    System.out.println("test annotation");
  }

  @After
  public void displayAfer() {
    System.out.println("After Annotation");
  }
}
