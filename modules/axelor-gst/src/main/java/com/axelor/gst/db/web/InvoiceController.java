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
    List<InvoiceLine> invoiceLineLis = invoice.getInvoiceLineList();
    double cgst = 0, sgst = 0, igst = 0, netTotal = 0, grossTotal = 0;

    for (InvoiceLine invoiceLine : invoiceLineLis) {

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
}
