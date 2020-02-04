package com.axelor.gst.sequence.test.standard;

import com.axelor.app.AppModule;
import com.axelor.auth.AuthModule;
import com.axelor.db.JpaModule;
import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new JpaModule("testUnit", true, true));
    install(new AuthModule());
    install(new AppModule());
  }
}
