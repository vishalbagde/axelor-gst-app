package com.axelor.gst.db.repo;

import com.axelor.gst.db.Party;
import java.util.Map;

public class GstPartyRepo extends PartyRepository {

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {

    try {
      Long id = (Long) json.get("id");
      Party party = find(id);
      json.put("address", party.getAddressList().get(0));
    } catch (Exception e) {
      System.err.println(e.fillInStackTrace().toString());
    }
    return json;
  }
}
