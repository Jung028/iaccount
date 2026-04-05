package com.alipay.account_center.biz.service.impl.account.impl;

import com.alipay.account_center.biz.service.impl.account.TopUpService;
import com.alipay.business.common.service.facade.event.EcTopUpEvent;
import org.springframework.stereotype.Service;

/**
 * @author adam
 * @date 4/4/2026 6:00 PM
 */
@Service
public class TopUpServiceImpl implements TopUpService {
    @Override
    public void processTopUpBalance(EcTopUpEvent event) {
        // first before this method is called, we need to insert an idempotnecy key into the table when user clicks on top up, createTopUpIntent. in PENDING status
        // then here we will check that the idempontcy key exists that is PENDING, for this user Id, then update to PROCESSING.



        // first we query the idempotency keys using the paymentMethodId (which we have inserted during createTopUpIntent, , to check if there is record of PENDING status
        // if exist and retry count is within limit, then we update to PROCESSING,
        // then we wrap a transactional template with.
        // then we lock the account.
        // update the account balance,
        // insert ledger entry for the account.
        // update the idempotnecy record to FINISHED., set reference id to the top up id, or payment intent id.
            // if fail, update idempotency to failed
            // if exceed max retires add to DLQ after retries.
        // unlock the account id.
        // publish the result EC_TOPUP_RESULT



    }
}