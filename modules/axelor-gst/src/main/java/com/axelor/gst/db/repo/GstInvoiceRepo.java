package com.axelor.gst.db.repo;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import java.util.Map;

public class GstInvoiceRepo extends InvoiceRepository {

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {

    try {
      Long id = (Long) json.get("id");
      Invoice invoice = find(id);
      Party party = invoice.getParty();
      json.put("partyName", party.getName());
      json.put("image", invoice.getCompany().getLogo().getFilePath());
    } catch (Exception e) {
    }
    return json;
  }

  @Override
  public Invoice save(Invoice entity) {
    return super.save(entity);
  }
}
