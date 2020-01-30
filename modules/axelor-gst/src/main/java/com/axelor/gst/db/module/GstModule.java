package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.GstInvoiceRepo;
import com.axelor.gst.db.repo.GstPartyRepo;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.service.GstInvoiceLineService;
import com.axelor.gst.db.service.GstInvoiceLineServiceImpl;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.gst.db.service.GstInvoiceServiceImpl;
import com.axelor.gst.db.service.SequenceService;
import com.axelor.gst.db.service.SequenceServiceImpl;

public class GstModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(PartyRepository.class).to(GstPartyRepo.class);
    bind(InvoiceRepository.class).to(GstInvoiceRepo.class);
    bind(SequenceService.class).to(SequenceServiceImpl.class);
    bind(GstInvoiceService.class).to(GstInvoiceServiceImpl.class);
    bind(GstInvoiceLineService.class).to(GstInvoiceLineServiceImpl.class);
  }
}
