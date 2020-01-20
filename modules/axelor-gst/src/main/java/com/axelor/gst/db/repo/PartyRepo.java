package com.axelor.gst.db.repo;

import com.axelor.gst.db.Party;
import java.util.Map;

public class PartyRepo extends PartyRepository {

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {

    /*  if (!context.containsKey("json-enhance")) {
     return json;
    }
    */
    try {
      Long id = (Long) json.get("id");
      Party party = find(id);

      json.put("address", party.getAddress().get(0));
    } catch (Exception e) {
    }
    return json;
  }
}
