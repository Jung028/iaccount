package com.alipay.account_center.common.dal.auto.custom;


import com.alipay.account_center.common.dal.auto.dataobject.LedgerEntryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jung
 * @date 2026-02-14 16:46:51
 */
@Mapper
public interface LedgerEntryDAO {

    int insertLedgerEntry(LedgerEntryDO ledgerEntryDO);
}