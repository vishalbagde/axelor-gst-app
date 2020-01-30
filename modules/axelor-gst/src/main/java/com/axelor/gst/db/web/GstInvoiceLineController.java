package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.service.GstInvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstInvoiceLineController {
  @Inject GstInvoiceLineService invoiceLineSer;

  public void calculateGst(ActionRequest request, ActionResponse response) {

    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    if (invoice != null) {

      if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {

        Boolean isIgst = false;
        if (invoice
            .getCompany()
            .getAddress()
            .getState()
            .equals(invoice.getInvoiceAddress().getState())) isIgst = false;
        else isIgst = true;

        invoiceLineSer.calculatePerProductGst(invoiceLine, isIgst);
        response.setValues(invoiceLine);

      } else {
        invoiceLine = invoiceLineSer.setEmptyValueInInvoiceLine(invoiceLine);
        response.setValues(invoiceLine);
        response.setFlash("Not find Party address or Invoice Address");
      }

    } else {
      response.setFlash("Invalid Invoice");
    }
  }

  public void setDefaultValueInInvoiceLine(ActionRequest request, ActionResponse response) {

    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    if (invoiceLine.getProduct() != null) {
      invoiceLineSer.setDefaultValueInInvoiceLine(invoiceLine);
      response.setValues(invoiceLine);
    } else {
      invoiceLine = invoiceLineSer.setEmptyValueInInvoiceLine(invoiceLine);
      response.setValues(invoiceLine);
    }
  }
}
