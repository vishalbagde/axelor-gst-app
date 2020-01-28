package com.axelor.gst.db.service;

import com.axelor.gst.db.Invoice;

public interface GstInvoiceService {

  public Invoice VerifyTotalInInvoice(Invoice invoice);

  public Invoice setTotalInInvoice(Invoice invoice);
}
