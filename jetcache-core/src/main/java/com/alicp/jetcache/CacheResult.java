package com.alicp.jetcache;

import com.alicp.jetcache.anno.CacheConsts;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * Created on 2016/9/28.
 *
 * @author huangli
 */
public class CacheResult {

    public static final String MSG_ILLEGAL_ARGUMENT = "illegal argument";

    private static Duration DEFAULT_TIMEOUT = CacheConsts.ASYNC_RESULT_TIMEOUT;

    public static final CacheResult SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.SUCCESS, null);
    public static final CacheResult PART_SUCCESS_WITHOUT_MSG = new CacheResult(CacheResultCode.PART_SUCCESS, null);
    public static final CacheResult FAIL_WITHOUT_MSG = new CacheResult(CacheResultCode.FAIL, null);
    public static final CacheResult FAIL_ILLEGAL_ARGUMENT = new CacheResult(CacheResultCode.FAIL, MSG_ILLEGAL_ARGUMENT);
    public static final CacheResult EXISTS_WITHOUT_MSG = new CacheResult(CacheResultCode.EXISTS, null);

    /**
     * result code 枚举，方便业务判断调用失败还是不存在
     */
    private volatile CacheResultCode resultCode;

    /**
     * 失败信息
     * 失败时是
     * @see MSG_ILLEGAL_ARGUMENT
     */
    private volatile String message;
    private final CompletionStage<ResultData> future;

    private volatile Duration timeout = DEFAULT_TIMEOUT;

    public CacheResult(CompletionStage<ResultData> future) {
        this.future = future;
    }

    public CacheResult(CacheResultCode resultCode, String message) {
        this(CompletableFuture.completedFuture(new ResultData(resultCode, message, null)));
    }

    public CacheResult(Throwable ex) {
        future = CompletableFuture.completedFuture(new ResultData(ex));
    }

    public boolean isSuccess() {
        return getResultCode() == CacheResultCode.SUCCESS;
    }

    protected void waitForResult() {
        waitForResult(timeout);
    }

    public void waitForResult(Duration timeout) {
        if (resultCode != null) {
            return;
        }
        try {
            ResultData resultData = future.toCompletableFuture().get(
                    timeout.toMillis(), TimeUnit.MILLISECONDS);
            fetchResultSuccess(resultData);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            fetchResultFail(e);
        }
    }

    protected void fetchResultSuccess(ResultData resultData) {
        message = resultData.getMessage();
        resultCode = resultData.getResultCode();
    }

    protected void fetchResultFail(Throwable e) {
        message = e.getClass() + ":" + e.getMessage();
        resultCode = CacheResultCode.FAIL;
    }

    public CacheResultCode getResultCode() {
        waitForResult();
        return resultCode;
    }

    public String getMessage() {
        waitForResult();
        return message;
    }

    public CompletionStage<ResultData> future() {
        return future;
    }

    public static void setDefaultTimeout(Duration defaultTimeout) {
        DEFAULT_TIMEOUT = defaultTimeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }
}
