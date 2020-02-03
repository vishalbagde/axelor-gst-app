package com.axelor.gst.db.web;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstSequenceController {

  @Inject SequenceService sequenceService;

  public void setNextNumberOnCreate(ActionRequest request, ActionResponse response) {

    Sequence sequence = request.getContext().asType(Sequence.class);
    if (sequence.getNextNumber() == null) {
      String seq = sequenceService.getSequence(sequence.getModel().getName());
      if (seq != null) {
        response.setValue("nextNumber", seq);
      } else {
        response.setFlash("Sequence Not Available");
      }
    }
  }
}
