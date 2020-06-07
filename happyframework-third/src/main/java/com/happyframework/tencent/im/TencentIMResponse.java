package com.happyframework.tencent.im;

import com.alan344happyframework.exception.BizException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author alan
 * @date 18-8-15 下午12:25 *
 * <p>
 * 70402	参数非法。请检查必填字段是否填充，或者字段的填充是否满足协议要求。
 * 70403	发起操作者不是APP管理员，没有权限操作。
 * 70404	设置简单资料后端超时。
 * <p>
 * 80001	消息文本安全打击。
 * 60002	HTTP 解析错误 ，请检查 HTTP 请求 URL 格式。
 * 60003	HTTP 请求 JSON 解析错误，请检查 JSON 格式 。
 * 60004	请求 URL 或 JSON 包体中帐号或签名错误 。
 * 60005	请求 URL 或 JSON 包体中帐号或签名错误 。
 * 60006	appid 失效，请核对 appid 有效性 。
 * 60007	rest 接口调用频率超过限制，请降低请求频率 。
 * 60008	服务请求超时或 HTTP 请求格式错误，请检查并重试 。
 * 60009	请求资源错误，请检查请求 URL。
 * 60010	请求需要 App 管理员权限，请检查接口调用权限。
 * 60011	appid 请求频率超限，请降低请求频率。
 * 60012	REST 接口需要带 sdkappid，请检查请求 URL 中的 sdkappid。
 * 60013	HTTP 响应包 JSON 解析错误。
 * 60014	置换 ID 超时。
 * 60015	请求包体帐号类型错误，请确认帐号为字符串格式。
 */
@Getter
@Setter
@Slf4j
@ToString
public class TencentIMResponse {

    /**
     * 请求处理的结果，OK 表示处理成功，FAIL 表示失败，如果为 FAIL，ErrorInfo 带上失败原因。
     */
    private String ActionStatus;

    /**
     * 失败原因 。
     */
    private String ErrorInfo;

    /**
     * 错误码，0 为成功，其他为失败，可查询 错误码表 得到具体的原因。
     */
    private int ErrorCode;

    /**
     * 加人结果：0-失败；1-成功；2-已经是群成员；3-等待被邀请者确认。
     */
    private int Result;

    public static boolean responseHandler(Map<String, Object> tencentIMResponse) {
        if (null == tencentIMResponse) {
            throw new BizException("腾讯第三方处理失败");
        }

        if (!tencentIMResponse.containsKey("ActionStatus") || !"OK".equalsIgnoreCase(tencentIMResponse.get("ActionStatus").toString())) {
            log.error(tencentIMResponse.toString());
            return false;
        }
        return true;
    }

    /**
     * @param tencentIMResponse tencentIMResponse
     * @return -1 再请求 -2 失败
     */
    public static int friendResponseHandler(Map<String, Object> tencentIMResponse) {
        if (null == tencentIMResponse) {
            return -1;
        }

        if (!tencentIMResponse.containsKey("ActionStatus") || !"OK".equalsIgnoreCase(tencentIMResponse.get("ActionStatus").toString())) {
            log.error(tencentIMResponse.toString());
            return -2;
        } else {
            if (tencentIMResponse.containsKey("ResultItem")) {
                List<Map<String, Object>> resultItems = (List<Map<String, Object>>) tencentIMResponse.get("ResultItem");
                log.info("resultItems", resultItems);
                Map<String, Object> resultItem = resultItems.get(0);
                int resultCode = (int) resultItem.get("ResultCode");
                switch (resultCode) {
                    case 30519:
                        throw new BizException("关注数量已达上限");
                    case 30712:
                        return 30712;
                    default:
                        break;
                }
            }

            return 0;
        }
    }

    /**
     * 获取Integer 重试器
     *
     * @param attempt 尝试次数
     * @return {@link Retryer}
     */
    public static Retryer<Integer> getIntegerRetry(int attempt) {
        return RetryerBuilder.<Integer>newBuilder()
                .retryIfResult(integer -> !Arrays.asList(-2, 0, 30712).contains(integer))
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(attempt))
                .build();
    }
}
