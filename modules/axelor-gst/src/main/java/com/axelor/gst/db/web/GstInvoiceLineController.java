package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.service.GstInvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import java.math.BigDecimal;

public class GstInvoiceLineController {
  @Inject GstInvoiceLineService invoiceLineSer;

  public void calculateGst(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParentContext().asType(Invoice.class);
    if (invoice != null) {

      if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {
        Address companyAddress = invoice.getCompany().getAddress();
        Address partyAddress = invoice.getInvoiceAddress();

        invoiceLine.setSgst(BigDecimal.valueOf(0));
        invoiceLine.setCgst(BigDecimal.valueOf(0));
        invoiceLine.setIgst(BigDecimal.valueOf(0));
        BigDecimal gstAmount = BigDecimal.ZERO;
        gstAmount =
            invoiceLine
                .getNetAmount()
                .multiply(invoiceLine.getGstRate())
                .divide(BigDecimal.valueOf(100));

        if (companyAddress.getState().equals(partyAddress.getState())) {
          response.setValue("sgst", gstAmount.divide(BigDecimal.valueOf(2)));
          invoiceLine.setSgst(gstAmount.divide(BigDecimal.valueOf(2)));
          response.setValue("cgst", gstAmount.divide(BigDecimal.valueOf(2)));
          invoiceLine.setCgst(gstAmount.divide(BigDecimal.valueOf(2)));

        } else {
          response.setValue("igst", gstAmount);
          invoiceLine.setIgst(gstAmount);
        }
        response.setValue("grossAmount", invoiceLine.getNetAmount().add(gstAmount));
        invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(gstAmount));
        response.setValues(invoiceLine);
      } else {
        response.setFlash("Not find Party address or Invoice Address");
        response.setReload(true);
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
      response.setFlash("Invalid Product");
    }
  }
}
