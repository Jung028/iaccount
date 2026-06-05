package com.alipay.account_center.rpc;

import com.alipay.account_center.web.TraceContext;
import com.alipay.sofa.rpc.context.RpcInvokeContext;
import com.alipay.sofa.rpc.core.exception.SofaRpcException;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.alipay.sofa.rpc.ext.Extension;
import com.alipay.sofa.rpc.filter.AutoActive;
import com.alipay.sofa.rpc.filter.Filter;
import com.alipay.sofa.rpc.filter.FilterInvoker;

import java.util.UUID;

/**
 * Provider-side SOFA RPC filter that restores the caller's traceId into the
 * logging MDC for RPC (bolt/rest) calls. The servlet {@code TraceIdFilter} does
 * not run on the SOFA REST (Netty) path, so without this the logback
 * {@code %X{traceId}} comes out empty for cross-service calls.
 *
 * Reads the "traceId" from RPC baggage (set by the caller's consumer filter);
 * falls back to a fresh UUID for direct/uncorrelated calls.
 */
@Extension("traceIdProvider")
@AutoActive(providerSide = true)
public class TraceIdProviderFilter extends Filter {

    private static final String TRACE_ID = "traceId";

    @Override
    public SofaResponse invoke(FilterInvoker invoker, SofaRequest request) throws SofaRpcException {
        try {
            String traceId = RpcInvokeContext.getContext().getRequestBaggage(TRACE_ID);
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString();
            }
            TraceContext.set(traceId);
            return invoker.invoke(request);
        } finally {
            TraceContext.clear();
        }
    }
}
