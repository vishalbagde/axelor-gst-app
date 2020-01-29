package com.axelor.gst.db.service;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.GstInvoiceRepo;

public interface GstInvoiceLineService {

  public InvoiceLine setDefaultValueInInvoiceLine(InvoiceLine invoiceLine);

  public InvoiceLine setEmptyValueInInvoiceLine(InvoiceLine invoiceLine);

  public InvoiceLine calculatePerProductGst(InvoiceLine invoiceLine, GstInvoiceRepo.Gst gst);
}
