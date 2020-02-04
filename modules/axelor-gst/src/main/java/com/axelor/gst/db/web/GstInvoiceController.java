package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstInvoiceController {

  @Inject GstInvoiceService invoiceSer;
  @Inject SequenceService sequenceService;
  @Inject SequenceRepository sequenceRepo;

  public void setReferenceInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    if (invoice.getReference() == null) {
      Sequence sequence =
          sequenceRepo
              .all()
              .filter("self.model.name = :model")
              .bind("model", Invoice.class.getSimpleName())
              .fetchOne();
      String seq = sequenceService.getSequence(sequence, true);
      if (seq != null) {
        invoice.setReference(seq);
        response.setValue("reference", seq);
      } else {
        response.setError("Sequence Not Available");
      }
    }
  }

  public void setOrderLineFromProduct(ActionRequest request, ActionResponse response) {
    String productIdsStr = (String) request.getContext().get("productIdsStr");

    if (productIdsStr != null) {
      String[] productIdsList = productIdsStr.split(",");

      response.setValue(
          "invoiceLineList", invoiceSer.setProductInInvoiceLineFromProduct(productIdsList));
    }
  }

  public void setPrimaryContactAndAddressInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = invoiceSer.setPrimaryContactAndAddress(invoice);
    response.setValues(invoice);
  }

  public void setTotalInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = invoiceSer.setTotalInInvoice(invoice);
    response.setValues(invoice);
  }

  public void setVerifyTotalInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice != null) {
      invoice = invoiceSer.verifyTotalInInvoice(invoice);

      response.setValue("invoiceLineList", invoice.getInvoiceLineList());

      response.setValue("cgst", invoice.getCgst());
      response.setValue("sgst", invoice.getSgst());
      response.setValue("igst", invoice.getIgst());
      response.setValue("netAmount", invoice.getNetAmount());
      response.setValue("grossAmount", invoice.getGrossAmount());

    } else {
      response.setFlash("No Invoice Found");
    }
  }
}
