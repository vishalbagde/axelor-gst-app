package com.axelor.gst.db.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import java.util.List;

public interface GstInvoiceService {

  public Invoice verifyTotalInInvoice(Invoice invoice);

  public Invoice setTotalInInvoice(Invoice invoice);

  public List<InvoiceLine> setProductInInvoiceLineFromProduct(String[] productIdsArray);

  public Invoice setPrimaryContactAndAddress(Invoice invoice);
}
