package com.axelor.gst.db.web;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;


public class InvoiceLineController {

  public void calculateGst(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParentContext().asType(Invoice.class);

    Address companyAddress = invoice.getCompany().getAddress();
    Address partyAddress = invoice.getInvoiceAddress();

    float gstAmount = 0;
    gstAmount =
        invoiceLine.getNetAmount().floatValue() * invoiceLine.getGstRate().floatValue() / 100;
    if (companyAddress.getState().equals(partyAddress.getState())) {
      response.setValue("sgst", BigDecimal.valueOf(gstAmount / 2));
      invoiceLine.setSgst(BigDecimal.valueOf(gstAmount / 2));
      response.setValue("cgst", BigDecimal.valueOf(gstAmount / 2));
      invoiceLine.setCgst(BigDecimal.valueOf(gstAmount / 2));
    } else {
      response.setValue("igst", BigDecimal.valueOf(gstAmount));
      invoiceLine.setIgst(BigDecimal.valueOf(gstAmount));
    }

    response.setValue("grossAmount", invoiceLine.getNetAmount().floatValue() + gstAmount);
    invoiceLine.setGrossAmount(BigDecimal.valueOf(invoiceLine.getNetAmount().floatValue() + gstAmount));
    invoice.setIgst(BigDecimal.valueOf(gstAmount));
    response.setValues(invoiceLine);
    
  }
}
