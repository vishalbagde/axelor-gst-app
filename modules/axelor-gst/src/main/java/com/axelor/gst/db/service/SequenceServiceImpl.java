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
	public String getSequence(Sequence sequence1, boolean isPersist) {

		Sequence sequence = sequence1;
		String seq = "";
		String nextNumber = "";
		String nextSeq = "";

		if (sequence != null) {

			if (sequence.getNextNumber() == null) {
				nextNumber = "1";
			} else {
				seq = sequence.getNextNumber();
				nextNumber = sequence.getNextNumber();

				if (sequence.getPrefix() != null)
					nextNumber = nextNumber.replace(sequence.getPrefix(), "");

				if (sequence.getSuffix() != null)
					nextNumber = nextNumber.replace(sequence.getSuffix(), "");

				if (StringUtils.isNumeric(nextNumber)) {
					nextNumber = Integer.parseInt(nextNumber) + 1 + "";

				} else {
					nextNumber = "1";
				}
			}
		} else {
			return null;
		}

		nextSeq += sequence.getPrefix();
		nextSeq += StringUtils.leftPad(nextNumber + "", sequence.getPadding(), "0");
		if (sequence.getSuffix() != null)
			nextSeq += sequence.getSuffix();
		sequence.setNextNumber(nextSeq);

		if (isPersist) {
			sequenceRepo.persist(sequence);
		}
		if (seq.equals(""))
			return nextSeq;
		else
			return seq;
	}
}



