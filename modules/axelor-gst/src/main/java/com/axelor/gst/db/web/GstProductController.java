package com.axelor.gst.db.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.base.Joiner;
import java.util.List;

public class GstProductController {

  public void gstSelectProductIdInGrid(ActionRequest request, ActionResponse response) {
    List<Integer> productIds = (List<Integer>) request.getContext().get("_ids");
    if (productIds != null && !productIds.isEmpty()) {

    	String productidStr = Joiner.on(',').join(productIds); 
      // System.err.println(productidStr);
      // response.setView(ActionView.define("Create Invoice").add("form",
      // "invoice-from").context("productIdsStr", productIds).map());
      request.getContext().put("_productIdsStr", productidStr);
    }
  }
}
