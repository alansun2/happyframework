package com.alan344happyframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
    public static final String PHONE_FORMAT = "^1[0-9]{10}$";

    public static final String PHONE_FORMAT_NEW = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    public static final String PHONE_FORMAT_NEW1 = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    public static final String PASSWORD_FORMAT = "^[0-9A-Za-z_]{6,18}$";

    public static final String GUID_FORMAT = "^[0-9]{1,10}$";

    public static final String IMAGE_FORMAT = "^\\.(jpg|jpeg)$";

    public static final String NICK_NAME_FORMAT = "[^\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]{1,8}";

    public static final String ROOM_ADDRESS_FORMAT = ".{1,25}";
    public static final String ROOM_ADDRESS_FOR = ".{1,55}";

    public static final String NICKNAME_FORMAT = ".{1,8}";

    public static final String HELP_CONTENT_FORMAT = "([^\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]|\t|\n|\r){1,120}";

    public static final String EMOJI_FORMAT = "[^\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]{1,150}";

    public static final String REMARK_EMOJI_FORMAT = "[^\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]{1,60}";

    public static final String INVITE_CODE_FORMAT = "^[0-9]{4}$";

    public static final Pattern PHONE_START_PATTERN = Pattern.compile(CheckUtils.PHONE_START);

    public static final String PHONE_START = "(?<=\\d{3})\\d(?=\\d{4})";

    /**
     * 匹配中文
     */
    public static final String ZH_PATTERN = "([\\u4e00-\\u9fa5]+)";

    /**
     * 匹配数字
     */
    public static final String NUMBER_PATTERN = "-?[1-9]\\d*";

    /**
     * 字母
     */
    public static final String LETTER_PATTERN = "^[A-Za-z]+";

    /**
     * 校验邀请码
     *
     * @param inviteCode
     * @return
     */
    public static boolean checkInviteCode(String inviteCode) {
        Pattern pattern = Pattern.compile(INVITE_CODE_FORMAT);
        Matcher matcher = pattern.matcher(inviteCode);
        return !matcher.matches();
    }

    /**
     * 反馈内容校验
     *
     * @param content
     * @return
     */
    public static boolean checkReedbackContent(String content) {
        Pattern pattern = Pattern.compile(EMOJI_FORMAT);
        Matcher matcher = pattern.matcher(content);
        return !matcher.matches();
    }

    /**
     * 备注字数校验
     *
     * @param remark
     * @return
     */
    public static boolean checkRemark(String remark) {
        Pattern pattern = Pattern.compile(REMARK_EMOJI_FORMAT);
        Matcher matcher = pattern.matcher(remark);
        return !matcher.matches();
    }

    /**
     * 收货内容字数校验
     *
     * @param content
     * @return
     */
    public static boolean checkReceiveContentLenth(String content) {
        if (content.length() > 45) {
            return true;
        }
        return false;
    }

    /**
     * 帮帮忙小费金额校验
     *
     * @param tip
     * @return
     */
    public static boolean checkHelpTip(int tip) {
        if (tip < 6 || tip > 30) {
            return true;
        }
        return false;
    }

    /**
     * 校验帮帮忙需求内容
     *
     * @param content
     * @return
     */
    public static boolean checkHelpContent(String content) {
        Pattern pattern = Pattern.compile(HELP_CONTENT_FORMAT);
        Matcher matcher = pattern.matcher(content);
        return !matcher.matches();
    }

    /**
     * 校验联系人姓名
     *
     * @param linkman
     * @return
     */
    public static boolean checkLinkman(String linkman) {
        Pattern pattern = Pattern.compile(NICKNAME_FORMAT);
        Matcher matcher = pattern.matcher(linkman);
        return !matcher.matches();
    }

    /**
     * 校验房间地址
     *
     * @param roomAddress
     * @return
     */
    public static boolean checkRoomAddress(String roomAddress) {
        Pattern pattern = Pattern.compile(ROOM_ADDRESS_FORMAT);
        Matcher matcher = pattern.matcher(roomAddress);
        return !matcher.matches();
    }

    /**
     * 校验房间地址
     *
     * @param roomAddress
     * @return
     */
    public static boolean checkRoomAddres(String roomAddress) {
        Pattern pattern = Pattern.compile(ROOM_ADDRESS_FOR);
        Matcher matcher = pattern.matcher(roomAddress);
        return !matcher.matches();
    }

    /**
     * 校验昵称
     *
     * @param nickName
     * @return
     */
    public static boolean checkNickName(String nickName) {
        Pattern pattern = Pattern.compile(NICK_NAME_FORMAT);
        Matcher matcher = pattern.matcher(nickName);
        return !matcher.matches();
    }

    /**
     * 验证图片格式
     *
     * @param imageType
     * @return
     */
    public static boolean checkImageType(String imageType) {
        Pattern pattern = Pattern.compile(IMAGE_FORMAT);
        Matcher matcher = pattern.matcher(imageType);
        return !matcher.matches();
    }

    /**
     * 验证用户id
     *
     * @param guid
     * @return
     */
    public static boolean checkGuid(String guid) {
        Pattern pattern = Pattern.compile(GUID_FORMAT);
        Matcher matcher = pattern.matcher(guid);
        return !matcher.matches();
    }

    /**
     * 验证手机号格式是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile(PHONE_FORMAT);
        Matcher matcher = pattern.matcher(phone);
        return !matcher.matches();
    }

    /**
     * 验证密码格式是否正确
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_FORMAT);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    /**
     * 手机号替换成星号
     *
     * @param phone
     * @return
     */
    public static String replacePhoneStart(String phone) {
        return PHONE_START_PATTERN.matcher(phone).replaceAll("*");
    }

    public static void main(String[] args) {
        System.out.println(CheckUtils.checkPhoneNumber("18351973590"));//

    }

    /**
     * 验证手机号格式是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkPhoneNumber(long phone) {
        Pattern pattern = Pattern.compile(PHONE_FORMAT);
        Matcher matcher = pattern.matcher(String.valueOf(phone));
        return !matcher.matches();
    }
}
