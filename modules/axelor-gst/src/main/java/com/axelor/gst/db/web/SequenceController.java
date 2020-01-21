package com.axelor.gst.db.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {

  @Inject SequenceService sequenceService;

  public void setReferenceInParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);
    
    if(party.getId()==null)
    {
    String seq = sequenceService.getSequence("Party");
    party.setReference(seq);
    response.setValue("reference", seq);
    }
  }
}
