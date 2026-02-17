package com.alipay.alipay_plus.common.util;

import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseRequest;
import com.alipay.alipay_plus.common.service.facade.baseresult.AccountBaseResult;
import com.alipay.alipay_plus.common.service.facade.enums.AccountResultCode;
import com.alipay.alipay_plus.core.model.exception.IdigitalriskException;
import org.slf4j.Logger;

public class LogUtil {
    public static final String SEPARATOR = ",";
    public static final char RIGHT_TAG = ']';
    public static final char LEFT_TAG = '[';

    public static void info(Logger logger, Object msg) {
        if(logger.isInfoEnabled()) {
            logger.info(msg.toString());
        }
    }
    public static void warn(Logger logger, Object msg) {
        if(logger.isWarnEnabled()) {
            logger.warn(msg.toString());
        }
    }
    public static void error(Logger logger, Object msg) {
        if (logger.isErrorEnabled()) {
            logger.error(msg.toString());
        }
    }

    public static <T extends AccountBaseRequest> void warn(Logger logger, IdigitalriskException e,
                                                                String s, T request, String s1, String s2,
                                                                AccountResultCode resultCode, String s3,
                                                                String message) {
        if (logger.isWarnEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(s)
                    .append(LEFT_TAG)
                    .append(request)
                    .append(RIGHT_TAG)
                    .append(s1)
                    .append(resultCode)
                    .append(s3)
                    .append(message);
            logger.warn(logBuilder.toString(), e);
        }
    }

    public static <R extends AccountBaseResult, T extends AccountBaseRequest> void info(Logger logger, String s, R result, String s1, T request, String s2) {
        if (logger.isInfoEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(s)
                    .append(LEFT_TAG)
                    .append(result)
                    .append(RIGHT_TAG)
                    .append(s1)
                    .append(LEFT_TAG)
                    .append(request)
                    .append(RIGHT_TAG);
            logger.info(logBuilder.toString());
        }
    }

    public static <T extends AccountBaseRequest> void error(Logger logger, Throwable e, String s, T request, String s1) {
        if (logger.isErrorEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(s)
                    .append(LEFT_TAG)
                    .append(request)
                    .append(RIGHT_TAG);
            logger.error(logBuilder.toString(), e);
        }
    }

    public static <T extends AccountBaseRequest> void info(Logger logger, String s, T request, String s1) {
        if (logger.isInfoEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(s)
                    .append(LEFT_TAG)
                    .append(request)
                    .append(RIGHT_TAG);
            logger.info(logBuilder.toString());
        }
    }

    public static void error(Logger logger, InterruptedException e, String message) {
        if (logger.isErrorEnabled()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(message);
            logger.error(logBuilder.toString(), e);
        }
    }
}
