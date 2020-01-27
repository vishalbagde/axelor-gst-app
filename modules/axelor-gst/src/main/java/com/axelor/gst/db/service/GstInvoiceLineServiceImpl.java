package com.axelor.gst.db.service;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import java.math.BigDecimal;

public class GstInvoiceLineServiceImpl implements GstInvoiceLineService {

  @Override
  public InvoiceLine setDefaultValueInInvoiceLine(InvoiceLine invoiceLine) {

    Product product = invoiceLine.getProduct();
    if (product != null) {
      invoiceLine.setItem("[" + product.getCode() + "] " + product.getName());
      invoiceLine.setPrice(product.getSalePrice());
      invoiceLine.setHsbn(product.getHsbn());
      invoiceLine.setQty(1);
      invoiceLine.setGstRate(product.getGstRate());
      invoiceLine.setNetAmount(
          invoiceLine.getPrice().multiply(BigDecimal.valueOf(invoiceLine.getQty())));
      return invoiceLine;
    } else return null;
  }
}
