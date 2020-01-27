package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.service.GstInvoiceLineService;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceController {

  @Inject GstInvoiceService invoiceService;
  @Inject ProductRepository productRepo;
  @Inject GstInvoiceLineService invoiceLineSer;

  public void setOrderLineFromProduct(ActionRequest request, ActionResponse response) {
    String productIdsStr = (String) request.getContext().get("productIdsStr");
    if (productIdsStr != null) {
      Invoice invoice = request.getContext().asType(Invoice.class);
      List<InvoiceLine> invoiceLineList = new ArrayList<InvoiceLine>();
      String[] productIdsList = productIdsStr.split(",");
      for (int i = 0; i < productIdsList.length; i++) {
        InvoiceLine invoiceline = new InvoiceLine();
        Product product =
        productRepo.all().filter("self.id = :id").bind("id", productIdsList[i]).fetchOne();
        invoiceline.setProduct(product);
        invoiceline=invoiceLineSer.setDefaultValueInInvoiceLine(invoiceline);
        invoiceLineList.add(invoiceline);
        invoice.setInvoiceLineList(invoiceLineList);
      }
      response.setValue("invoiceLineList", invoiceLineList);
    }
  }

  public void setPrimaryContactAndAddressInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    try {
      if (party != null) {
        List<Contact> contactList = party.getContactList();
        if (contactList != null && !contactList.isEmpty()) {
          for (Contact contact : contactList) {
            if (contact.getTypeSelect().equals("primary")) {
              response.setValue("partyContact", contact);
              break;
            }
          }
          List<Address> addressList = party.getAddressList();
          for (Address address : addressList) {
            if (address.getTypeSelect().equals("default")
                || address.getTypeSelect().equals("invoice")) {
              response.setValue("invoiceAddress", address);
              break;
            }
          }
          if (!invoice.getIsInvoiceAddAsShippingAdd()) {
            for (Address address : addressList) {
              if (address.getTypeSelect().equals("default")
                  || address.getTypeSelect().equals("shipping")) {
                response.setValue("shippingAddress", address);
                break;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      response.setFlash(e.fillInStackTrace().toString());
    }
  }

  public void setTotalInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = invoiceService.setTotalInInvoice(invoice);
    response.setValues(invoice);

    /*		response.setValue("igst", invoice.getIgst());
    		response.setValue("cgst", invoice.getCgst());
    		response.setValue("sgst", invoice.getSgst());
    		response.setValue("netAmount", invoice.getNetAmount());
    		response.setValue("grossAmount", invoice.getGrossAmount());
    */
  }

  public void setVerifyTotalInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice != null) {
      invoice = invoiceService.VerifyTotalInInvoice(invoice);

      response.setValue("invoiceLineList", invoice.getInvoiceLineList());
      response.setValue("igst", invoice.getIgst());
      response.setValue("cgst", invoice.getCgst());
      response.setValue("sgst", invoice.getSgst());
      response.setValue("netAmount", invoice.getNetAmount());
      response.setValue("grossAmount", invoice.getGrossAmount());

    } else {
      response.setFlash("No Invoice Found");
    }
  }
}
