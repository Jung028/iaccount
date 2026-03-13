package com.alipay.account_center.core.service.repository.impl;

import com.alipay.account_center.common.dal.auto.dataobject.LedgerEntryDO;
import com.alipay.account_center.common.service.facade.item.LedgerEntryItem;
import com.alipay.account_center.core.model.exception.RepositoryException;
import com.alipay.account_center.core.service.repository.AbstractDomainRepository;
import com.alipay.account_center.core.service.repository.AccountLedgerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountLedgerRepositoryImpl extends AbstractDomainRepository implements AccountLedgerRepository {
    @Override
    public LedgerEntryDO insertLedger(LedgerEntryItem ledgerEntryItem) {
        if (ledgerEntryItem == null) {
            return null;
        }
        try {
            LedgerEntryDO ledgerEntryDO = new LedgerEntryDO();
            ledgerEntryDO.setTxnId(ledgerEntryItem.getTxnId());
            ledgerEntryDO.setAccountId(ledgerEntryItem.getAccountId());
            ledgerEntryDO.setEntryType(ledgerEntryItem.getEntryType());
            ledgerEntryDO.setAmount(ledgerEntryItem.getAmount());
            ledgerEntryDO.setCurrency(ledgerEntryItem.getCurrency());
            ledgerEntryDO.setBalanceAfter(ledgerEntryItem.getBalanceAfter());
            ledgerEntryDO.setStatus("POSTED");
            ledgerEntryDO.setRemark(ledgerEntryItem.getRemark());

            int rows = ledgerEntryDAO.insertLedgerEntry(ledgerEntryDO);
            if (rows <= 0) {
                throw new RepositoryException("Insert failed for txnId: " + ledgerEntryItem.getTxnId());
            }
            return ledgerEntryDO;
        } catch (RepositoryException e) {
            throw new RepositoryException("Insert failed for txnId: " + ledgerEntryItem.getTxnId(), e);
        } catch (Exception e) {
            throw new RepositoryException("DB error during insert ledger entry", e);
        }
    }
}