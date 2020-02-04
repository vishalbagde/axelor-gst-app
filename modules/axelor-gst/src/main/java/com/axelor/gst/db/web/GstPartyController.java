package com.axelor.gst.db.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstPartyController {

  @Inject SequenceService sequenceService;
  @Inject SequenceRepository sequenceRepo;

  public void setReferenceInParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);

    if (party.getReference() == null) {
      Sequence sequence =
          sequenceRepo
              .all()
              .filter("self.model.name = :model")
              .bind("model", Party.class.getSimpleName())
              .fetchOne();
      String seq = sequenceService.getSequence(sequence, true);
      if (seq != null) {
        party.setReference(seq);
        response.setValue("reference", seq);
      } else {
        response.setFlash("Sequence Not Available");
      }
    }
  } // end method
} // end class
