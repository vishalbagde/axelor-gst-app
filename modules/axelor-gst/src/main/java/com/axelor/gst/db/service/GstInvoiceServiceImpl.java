package com.axelor.gst.db.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.GstInvoiceRepo;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.util.List;

public class GstInvoiceServiceImpl implements GstInvoiceService {
  @Inject GstInvoiceRepo invoiceRepo;

  @Transactional
  @Override
  public Invoice VerifyTotalInInvoice(Invoice invoice) {

    if (invoice != null) {
      List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();
      BigDecimal cgst = BigDecimal.ZERO;
      BigDecimal sgst = BigDecimal.ZERO;
      BigDecimal igst = BigDecimal.ZERO;
      BigDecimal netTotal = BigDecimal.ZERO;
      BigDecimal grossTotal = BigDecimal.ZERO;

      if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {
        Address companyAddress = invoice.getCompany().getAddress();
        Address partyAddress = invoice.getInvoiceAddress();

        if (invoiceLineList != null) {
          for (int i = 0; i < invoiceLineList.size(); i++) {

            InvoiceLine invoiceLine = invoiceLineList.get(i);
            invoiceLine.setSgst(BigDecimal.valueOf(0));
            invoiceLine.setCgst(BigDecimal.valueOf(0));
            invoiceLine.setIgst(BigDecimal.valueOf(0));

            BigDecimal gstAmount = BigDecimal.ZERO;
            invoiceLine.setNetAmount(
                BigDecimal.valueOf(
                    invoiceLine.getPrice().doubleValue() * invoiceLine.getQty().doubleValue()));
            gstAmount =
                invoiceLine
                    .getNetAmount()
                    .multiply(invoiceLine.getGstRate())
                    .divide(BigDecimal.valueOf(100));

            if (companyAddress.getState().equals(partyAddress.getState())) {
              invoiceLine.setSgst(gstAmount.divide(BigDecimal.valueOf(2)));
              invoiceLine.setCgst(gstAmount.divide(BigDecimal.valueOf(2)));
            } else {
              invoiceLine.setIgst(gstAmount);
            }
            invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(gstAmount));

            invoiceLineList.set(i, invoiceLine);

            netTotal = netTotal.add(invoiceLine.getNetAmount());
            sgst = sgst.add(invoiceLine.getSgst());
            cgst = cgst.add(invoiceLine.getCgst());
            igst = igst.add(invoiceLine.getIgst());
            grossTotal = grossTotal.add(invoiceLine.getGrossAmount());
          }
          invoice.setInvoiceLineList(invoiceLineList);
          invoice.setIgst(igst);
          invoice.setCgst(cgst);
          invoice.setSgst(sgst);
          invoice.setNetAmount(netTotal);
          invoice.setGrossAmount(grossTotal);
        }
      }
      invoiceRepo.persist(invoice);
      return invoice;

    } else return null;
  }
}
