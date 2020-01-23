package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceController {

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
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();

    double cgst = 0, sgst = 0, igst = 0, netTotal = 0, grossTotal = 0;

    for (InvoiceLine invoiceLine : invoiceLineList) {

      netTotal += invoiceLine.getNetAmount().doubleValue();
      sgst += invoiceLine.getSgst().doubleValue();
      cgst += invoiceLine.getCgst().doubleValue();
      igst += invoiceLine.getIgst().doubleValue();
      grossTotal += invoiceLine.getGrossAmount().floatValue();
    }
    response.setValue("igst", BigDecimal.valueOf(igst));
    response.setValue("cgst", BigDecimal.valueOf(cgst));
    response.setValue("sgst", BigDecimal.valueOf(sgst));
    response.setValue("netAmount", BigDecimal.valueOf(netTotal));
    response.setValue("grossAmount", BigDecimal.valueOf(grossTotal));
  }

  public void setVerifyTotalInInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();
    double cgst = 0, sgst = 0, igst = 0, netTotal = 0, grossTotal = 0;

    if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {

      Address companyAddress = invoice.getCompany().getAddress();
      Address partyAddress = invoice.getInvoiceAddress();

      for (int i = 0; i < invoiceLineList.size(); i++) {
        InvoiceLine invoiceLine = invoiceLineList.get(i);

        invoiceLine.setSgst(BigDecimal.valueOf(0));
        invoiceLine.setCgst(BigDecimal.valueOf(0));
        invoiceLine.setIgst(BigDecimal.valueOf(0));

        double gstAmount = 0;
        invoiceLine.setNetAmount(
            BigDecimal.valueOf(
                invoiceLine.getPrice().doubleValue() * invoiceLine.getQty().doubleValue()));
        gstAmount =
            invoiceLine.getNetAmount().doubleValue() * invoiceLine.getGstRate().doubleValue() / 100;
        if (companyAddress.getState().equals(partyAddress.getState())) {
          invoiceLine.setSgst(BigDecimal.valueOf(gstAmount / 2));
          invoiceLine.setCgst(BigDecimal.valueOf(gstAmount / 2));
        } else {
          invoiceLine.setIgst(BigDecimal.valueOf(gstAmount));
        }
        invoiceLine.setGrossAmount(
            BigDecimal.valueOf(invoiceLine.getNetAmount().doubleValue() + gstAmount));

        invoiceLineList.set(i, invoiceLine);

        netTotal += invoiceLine.getNetAmount().doubleValue();
        sgst += invoiceLine.getSgst().doubleValue();
        cgst += invoiceLine.getCgst().doubleValue();
        igst += invoiceLine.getIgst().doubleValue();
        grossTotal += invoiceLine.getGrossAmount().floatValue();
      }
      invoice.setInvoiceLineList(invoiceLineList);
      invoice.setIgst(BigDecimal.valueOf(igst));
      invoice.setCgst(BigDecimal.valueOf(cgst));
      invoice.setSgst(BigDecimal.valueOf(sgst));
      invoice.setNetAmount(BigDecimal.valueOf(netTotal));
      invoice.setGrossAmount(BigDecimal.valueOf(grossTotal));

      response.setValues(invoice);
    } else {
      response.setFlash("Not find Party address or Invoice Address");
      response.setHidden("ok", true);
    }
  }
}
