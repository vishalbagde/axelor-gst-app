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

  @Override
  public InvoiceLine calculatePerProductGst(InvoiceLine invoiceLine, Boolean isIgst) {

    invoiceLine.setSgst(BigDecimal.ZERO);
    invoiceLine.setCgst(BigDecimal.ZERO);
    invoiceLine.setIgst(BigDecimal.ZERO);
    BigDecimal gstAmount = BigDecimal.ZERO;

    gstAmount =
        invoiceLine
            .getNetAmount()
            .multiply(invoiceLine.getGstRate())
            .divide(BigDecimal.valueOf(100));
    if (isIgst) {
      invoiceLine.setIgst(gstAmount);
    } else {
      BigDecimal interGst = gstAmount.divide(BigDecimal.valueOf(2));
      invoiceLine.setSgst(interGst);
      invoiceLine.setCgst(interGst);
    }
    invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(gstAmount));
    return invoiceLine;
  }

  @Override
  public InvoiceLine setEmptyValueInInvoiceLine(InvoiceLine invoiceLine) {
    invoiceLine.setProduct(null);
    invoiceLine.setItem(null);
    invoiceLine.setCgst(null);
    invoiceLine.setSgst(null);
    invoiceLine.setQty(0);
    invoiceLine.setPrice(null);
    return invoiceLine;
  }
}
