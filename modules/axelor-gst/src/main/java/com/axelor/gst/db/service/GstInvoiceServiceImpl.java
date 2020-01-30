package com.axelor.gst.db.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceServiceImpl implements GstInvoiceService {

  @Inject ProductRepository productRepo;
  @Inject GstInvoiceLineService invoiceLineSer;

  @Override
  public Invoice verifyTotalInInvoice(Invoice invoice) {

    if (invoice != null) {
      List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();

      if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {
        boolean isIgst = false;

        if (invoice
            .getCompany()
            .getAddress()
            .getState()
            .equals(invoice.getInvoiceAddress().getState())) isIgst = false;
        else isIgst = true;

        if (invoiceLineList != null) {

          for (InvoiceLine invoiceLine : invoiceLineList) {

            invoiceLine = invoiceLineSer.calculatePerProductGst(invoiceLine, isIgst);
          }
          invoice.setInvoiceLineList(invoiceLineList);
          invoice = setTotalInInvoice(invoice);
        }
      }
      return invoice;
    } else return null;
  }

  @Override
  public Invoice setTotalInInvoice(Invoice invoice) {

    List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();

    invoice.setCgst(BigDecimal.ZERO);
    invoice.setSgst(BigDecimal.ZERO);
    invoice.setIgst(BigDecimal.ZERO);
    invoice.setNetAmount(BigDecimal.ZERO);
    invoice.setGrossAmount(BigDecimal.ZERO);

    for (InvoiceLine invoiceLine : invoiceLineList) {

      invoice.setNetAmount(invoice.getNetAmount().add(invoiceLine.getNetAmount()));
      invoice.setSgst(invoice.getSgst().add(invoiceLine.getSgst()));
      invoice.setCgst(invoice.getCgst().add(invoiceLine.getCgst()));
      invoice.setIgst(invoice.getIgst().add(invoiceLine.getIgst()));
      invoice.setGrossAmount(invoice.getGrossAmount().add(invoiceLine.getGrossAmount()));
    }
    return invoice;
  }

  @Override
  public List<InvoiceLine> setProductInInvoiceLineFromProduct(String[] productIdsList) {

    List<InvoiceLine> invoiceLineList = new ArrayList<InvoiceLine>();

    for (String productIdStr : productIdsList) {
      InvoiceLine invoiceline = new InvoiceLine();
      Product product = productRepo.find(Long.parseLong(productIdStr));

      if (product != null) {
        invoiceline.setProduct(product);
        invoiceline = invoiceLineSer.setDefaultValueInInvoiceLine(invoiceline);
        invoiceLineList.add(invoiceline);
      }
    }
    return invoiceLineList;
  }

  @Override
  public Invoice setPrimaryContactAndAddress(Invoice invoice) {
    Party party = invoice.getParty();
    try {
      if (party != null) {

        List<Contact> contactList = party.getContactList();
        Contact contact =
            contactList
                .stream()
                .filter(contactType -> "primary".equals(contactType.getTypeSelect()))
                .findAny()
                .orElse(null);

        invoice.setPartyContact(contact);

        List<Address> addressList = party.getAddressList();

        Address invoiceAddress =
            addressList
                .stream()
                .filter(
                    addressType ->
                        addressType.getTypeSelect().equals("default")
                            || addressType.getTypeSelect().equals("invoice"))
                .findAny()
                .orElse(null);
        invoice.setInvoiceAddress(invoiceAddress);

        Address shippingAddress =
            addressList
                .stream()
                .filter(
                    addressType ->
                        addressType.getTypeSelect().equals("default")
                            || addressType.getTypeSelect().equals("shipping"))
                .findAny()
                .orElse(null);
        invoice.setShippingAddress(shippingAddress);
      }

    } catch (Exception e) {
      System.err.println(e.fillInStackTrace().toString());
    }
    return invoice;
  }
}
