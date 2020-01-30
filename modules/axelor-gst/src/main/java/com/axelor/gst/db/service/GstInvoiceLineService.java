package com.axelor.gst.db.service;

import com.axelor.gst.db.InvoiceLine;

public interface GstInvoiceLineService {

  public InvoiceLine setDefaultValueInInvoiceLine(InvoiceLine invoiceLine);

  public InvoiceLine setEmptyValueInInvoiceLine(InvoiceLine invoiceLine);

  public InvoiceLine calculatePerProductGst(InvoiceLine invoiceLine, Boolean isIgst);
}
