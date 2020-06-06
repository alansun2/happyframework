package com.happyframework.tencent.im;

import com.alan344happyframework.util.RetryUtils;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.tls.tls_sigature.tls_sigature;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

/**
 * @author alan
 * @createtime 18-8-15 下午3:38 *
 * <p>
 * 腾讯IM
 */
@Slf4j
public class IMSignUtils {

    /**
     * 生成usersig，尝试3次
     *
     * @param appId       appId
     * @param identifier  管理员账号
     * @param accountType accountType
     * @param privStr     priStr
     * @return usersig
     */
    static String createUsersig(String appId, String identifier, long accountType, String privStr) {
        Callable<String> callable = () -> {
            tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(Long.valueOf(appId), identifier, privStr);
            if (0 == result.urlSig.length()) {
                log.error("GenTLSSignatureEx failed: " + result.errMessage);
                return null;
            }
            return result.urlSig;
        };

        Retryer<String> strRetry = RetryUtils.getStrRetry(3);
        try {
            return strRetry.call(callable);
        } catch (ExecutionException | RetryException e) {
            log.error("im error ", e);
        }
        return null;
    }


    /**
     * 判断是否过期
     *
     * @param urlSig      签名
     * @param appId       appId
     * @param identifier  id
     * @param accountType accountType
     * @param publicKey   公钥
     * @return true：未过期
     * @throws DataFormatException e
     */
    static boolean checkExpire(String urlSig, String appId, String identifier, long accountType, String publicKey) throws DataFormatException {
        tls_sigature.CheckTLSSignatureResult checkTLSSignatureResult = tls_sigature.CheckTLSSignatureEx(urlSig, Long.valueOf(appId), identifier, publicKey);
        return checkTLSSignatureResult.verifyResult;
    }

//    public static void main(String[] args) throws DataFormatException {
//        String sig = "eJxtz1FPwjAQB-Dv0meiXUe7aeLDojMRukUjKPGl6Vi7dKZ0q8eKEL67sMCbj3e-u9z-DmjB329k15laSBCxr9E9iqYY44SlNEGT0dWuM14JqUH5s1NKyWnkoqZWGzDajBZfd35McyqLfPn48tSvhpXuy91XzJpyzuC14XsO1YxZkgfv7UcAXhHtVJaZPBsWWa-fotu2SHER9tXQOgIs-*Sy4qwMbfDWOGpnz1v3cD1Wf4vxif-Sg7FqTB3fMUZwGl36cr122w0I*O3OTqZJxNDxD*qhUw4_";
//        String pub = "-----BEGIN PUBLIC KEY-----\n" +
//                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAERcir7NvYkM7IQUa0G37h+zw0O/oM\n" +
//                "p0yDzBwIwQg/4No/2f40Gw1V0nlkala08LqCitVs9D0i6pwTnmJ7eYSD+Q==\n" +
//                "-----END PUBLIC KEY-----";
//        boolean b = checkExpire(sig, "1400076857", "37", 24716, pub);
//        int i = 3;
//    }
}
