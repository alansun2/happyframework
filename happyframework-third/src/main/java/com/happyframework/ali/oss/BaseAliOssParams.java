package com.happyframework.ali.oss;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2019/7/3 10:07
 **/
@Getter
@Setter
abstract class BaseAliOssParams {
    public String bucketName;

    public String objectName;
}
