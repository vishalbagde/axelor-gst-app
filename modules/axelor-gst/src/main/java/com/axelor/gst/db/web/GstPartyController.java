package com.axelor.gst.db.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstPartyController {

  @Inject SequenceService sequenceService;

  public void setReferenceInParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);

    if (party.getReference() == null) {
      String seq = sequenceService.getSequence(Party.class.getSimpleName());
      if (seq != null) {
        party.setReference(seq);
        response.setValue("reference", seq);
      } else {
        response.setFlash("Sequence Not Available");
      }
    }
  } // end method
} // end class
