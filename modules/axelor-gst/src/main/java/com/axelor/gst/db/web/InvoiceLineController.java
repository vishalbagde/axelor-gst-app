package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {

  public void calculateGst(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
     response.setFlash(invoiceLine.getHsbn().toString());
     Invoice invoice = invoiceLine.getInvoice();
     response.setFlash(invoice.getInvoiceDateT().toString());
    
  }
}
