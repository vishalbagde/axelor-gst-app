package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.InvoiceRepo;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepo;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.gst.db.service.SequenceServiceImpl;

public class GstModule extends AxelorModule {

  @Override
  protected void configure() {

    bind(InvoiceRepository.class).to(InvoiceRepo.class);
    bind(PartyRepository.class).to(PartyRepo.class);
    bind(SequenceService.class).to(SequenceServiceImpl.class);
  }
}
