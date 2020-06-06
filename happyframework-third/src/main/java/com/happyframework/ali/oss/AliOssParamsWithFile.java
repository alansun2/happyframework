package com.happyframework.ali.oss;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

/**
 * @author alan
 * @createtime 18-8-10 下午2:22 *
 */
@Getter
@Setter
@ToString
public class AliOssParamsWithFile extends BaseAliOssParams {

    public File file;
}
