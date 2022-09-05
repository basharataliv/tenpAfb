package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.model.sqlserver.LPPOLNUM;
import com.afba.imageplus.repository.sqlserver.LPPOLNUMRepository;
import com.afba.imageplus.service.PolicyService;
import com.afba.imageplus.utilities.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PolicyServiceImpl implements PolicyService {

	Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);

	private final DateHelper dateHelper;
	private final LPPOLNUMRepository lppolnumRepository;

	@Autowired
	public PolicyServiceImpl(LPPOLNUMRepository lppolnumRepository,
							 DateHelper dataHelper) {
		this.lppolnumRepository = lppolnumRepository;
		this.dateHelper = dataHelper;
	}

	public LPPOLNUM createOrUpdateUniquePolicyAsPerCurrentJulianDate() {

		String julianDate = dateHelper.getCurrentJulianDate();
		Optional<LPPOLNUM> lppOptional = this.lppolnumRepository.findFirstBy();

		if (lppOptional.isPresent()) {

			LPPOLNUM lppolnum = lppOptional.get();

			if (!lppolnum.getJulianDate().equals(julianDate)) {

				lppolnum.setJulianDate(julianDate);
				lppolnum.setSequenceNumber(this.generateSequenceNumber(0));
			} else {
				lppolnum.setSequenceNumber(this.generateSequenceNumber(Integer.parseInt(lppolnum.getSequenceNumber())));
			}

			return createRecord(lppolnum);

		} else {
			return this.createRecord(new LPPOLNUM(julianDate, this.generateSequenceNumber(0)));
		}
	}

	private LPPOLNUM createRecord(LPPOLNUM lppolnum) {
		return lppolnumRepository.save(lppolnum);
	}

	private String generateSequenceNumber(Integer currSeqNum) {
		return String.format(ApplicationConstants.SEQUENCE_NUMBER_FORMAT, currSeqNum + 1);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String getUniquePolicyId() {
		LPPOLNUM lppolnum = createOrUpdateUniquePolicyAsPerCurrentJulianDate();
		return lppolnum.getJulianDate() + lppolnum.getSequenceNumber();
	}
}
