package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.base.Joiner;
import java.util.List;

public class GstProductController {

  public String getSelectedProductIds(List<?> productIds) {
    String productidStr = Joiner.on(',').join(productIds);
    return productidStr;
  }

  public void gstSelectProductIdInGrid(ActionRequest request, ActionResponse response) {
    List<Integer> productIds = (List<Integer>) request.getContext().get("_ids");
    if (productIds != null && !productIds.isEmpty()) {
      String productidStr = getSelectedProductIds(productIds);
      request.getContext().put("_productIdsStr", productidStr);
    }
  }

  public void gstCreateInvoiceInGrid(ActionRequest request, ActionResponse response) {
    List<Integer> productIds = (List<Integer>) request.getContext().get("_ids");
    if (productIds != null && !productIds.isEmpty()) {
      String productidStr = getSelectedProductIds(productIds);

      response.setView(
          ActionView.define("Create Invoice")
              .model(Invoice.class.getName())
              .add("form", "invoice-form")
              .context("productIdsStr", productidStr)
              .map());
    }
  }
}
