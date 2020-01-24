package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceController {

	public void setPrimaryContactAndAddressInInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		Party party = invoice.getParty();
		try {
			if (party != null) {
				List<Contact> contactList = party.getContactList();
				if (contactList != null && !contactList.isEmpty()) {
					for (Contact contact : contactList) {
						if (contact.getTypeSelect().equals("primary")) {
							response.setValue("partyContact", contact);
							break;
						}
					}

					List<Address> addressList = party.getAddressList();
					for (Address address : addressList) {
						if (address.getTypeSelect().equals("default") || address.getTypeSelect().equals("invoice")) {
							response.setValue("invoiceAddress", address);
							break;
						}
					}
					if (!invoice.getIsInvoiceAddAsShippingAdd()) {
						for (Address address : addressList) {
							if (address.getTypeSelect().equals("default")
									|| address.getTypeSelect().equals("shipping")) {
								response.setValue("shippingAddress", address);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			response.setFlash(e.fillInStackTrace().toString());
		}
	}

	public void setTotalInInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();

		// double cgst = 0, sgst = 0, igst = 0, netTotal = 0, grossTotal = 0;

		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal netTotal = BigDecimal.ZERO;
		BigDecimal grossTotal = BigDecimal.ZERO;

		for (InvoiceLine invoiceLine : invoiceLineList) {
			netTotal = netTotal.add(invoiceLine.getNetAmount());
			sgst = sgst.add(invoiceLine.getSgst());
			cgst = cgst.add(invoiceLine.getCgst());
			igst = igst.add(invoiceLine.getIgst());
			grossTotal = grossTotal.add(invoiceLine.getGrossAmount());
		}
		response.setValue("igst", igst);
		response.setValue("cgst", cgst);
		response.setValue("sgst", sgst);
		response.setValue("netAmount", netTotal);
		response.setValue("grossAmount", grossTotal);
	}

	public void setVerifyTotalInInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLineList = invoice.getInvoiceLineList();
		BigDecimal cgst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal netTotal = BigDecimal.ZERO;
		BigDecimal grossTotal = BigDecimal.ZERO;

		if (invoice.getCompany() != null && invoice.getInvoiceAddress() != null) {

			Address companyAddress = invoice.getCompany().getAddress();
			Address partyAddress = invoice.getInvoiceAddress();

			if (invoiceLineList != null) {
				for (int i = 0; i < invoiceLineList.size(); i++) {
					InvoiceLine invoiceLine = invoiceLineList.get(i);

					invoiceLine.setSgst(BigDecimal.valueOf(0));
					invoiceLine.setCgst(BigDecimal.valueOf(0));
					invoiceLine.setIgst(BigDecimal.valueOf(0));

					BigDecimal gstAmount = BigDecimal.ZERO;
					invoiceLine.setNetAmount(BigDecimal
							.valueOf(invoiceLine.getPrice().doubleValue() * invoiceLine.getQty().doubleValue()));
					gstAmount = invoiceLine.getNetAmount().multiply(invoiceLine.getGstRate())
							.divide(BigDecimal.valueOf(100));

					if (companyAddress.getState().equals(partyAddress.getState())) {
						invoiceLine.setSgst(gstAmount.divide(BigDecimal.valueOf(2)));
						invoiceLine.setCgst(gstAmount.divide(BigDecimal.valueOf(2)));
					} else {
						invoiceLine.setIgst(gstAmount);
					}
					invoiceLine.setGrossAmount(invoiceLine.getNetAmount().add(gstAmount));

					invoiceLineList.set(i, invoiceLine);

					netTotal = netTotal.add(invoiceLine.getNetAmount());
					sgst = sgst.add(invoiceLine.getSgst());
					cgst = cgst.add(invoiceLine.getCgst());
					igst = igst.add(invoiceLine.getIgst());
					grossTotal = grossTotal.add(invoiceLine.getGrossAmount());
				}
				invoice.setInvoiceLineList(invoiceLineList);
				invoice.setIgst(igst);
				invoice.setCgst(cgst);
				invoice.setSgst(sgst);
				invoice.setNetAmount(netTotal);
				invoice.setGrossAmount(grossTotal);
			}
			response.setValues(invoice);
		} else {
			response.setFlash("Not find Party address or Invoice Address");
			response.setHidden("ok", true);
		}
	}
}
