package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.GstPartyRepo;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.gst.db.service.SequenceServiceImpl;

public class GstModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(PartyRepository.class).to(GstPartyRepo.class);
    bind(SequenceService.class).to(SequenceServiceImpl.class);
  }
}
