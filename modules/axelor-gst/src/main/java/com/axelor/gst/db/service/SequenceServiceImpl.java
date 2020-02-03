package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.apache.commons.lang.StringUtils;
public class SequenceServiceImpl implements SequenceService {

	@Inject
	SequenceRepository sequenceRepo;

	@Transactional
	@Override
	public String getSequence(String model) {
		Sequence sequence = sequenceRepo.all().filter("self.model.name = :model").bind("model", model).fetchOne();
		if (sequence != null) {
			String seq = "";
			if (sequence.getNextNumber() == null) {
				int nextNumber = 1;
				seq += sequence.getPrefix();
				seq += StringUtils.leftPad(nextNumber + "", sequence.getPadding(), "0");
				if (sequence.getSuffix() != null) {
					seq += sequence.getSuffix();
				}
				// sequence.setNextNumber(Integer.parseInt(sequence.getNextNumber()) + 1 + "");
				// sequenceRepo.persist(sequence);
				return seq;
			} else {
				seq = sequence.getNextNumber();

				String nextNumber = sequence.getNextNumber();
				nextNumber = nextNumber.replace(sequence.getPrefix(), "");
				nextNumber = nextNumber.replace(sequence.getSuffix(), "");
				nextNumber = Integer.parseInt(nextNumber) + 1 + "";

				String nextSeq = "";
				nextSeq += sequence.getPrefix();
				nextSeq += StringUtils.leftPad(nextNumber + "", sequence.getPadding(), "0");
				if (sequence.getSuffix() != null) {
					nextSeq += sequence.getSuffix();
				}
				sequence.setNextNumber(nextSeq);
				sequenceRepo.persist(sequence);

				return seq;
			}
		} else {
			return null;
		}
	}
}