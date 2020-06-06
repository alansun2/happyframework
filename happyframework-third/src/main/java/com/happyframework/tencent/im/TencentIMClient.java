package com.happyframework.tencent.im;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author alan
 * @createtime 18-8-15 上午10:25 *
 */
@FeignClient(name = "tencent-Im", url = "https://console.tim.qq.com", path = "/v4/")
public interface TencentIMClient {
    /**
     * 独立模式账号导入
     * Identifier	String	必填	用户名，长度不超过 32 字节
     * Nick	        String	选填	用户昵称
     * FaceUrl	    String	选填	用户头像URL。
     * Type	        Integer	选填	帐号类型，开发者默认无需填写，值0表示普通帐号，1表示机器人帐号。
     *
     * @param baseRequest 基础参数
     * @param requestBody 请求参数 json
     * @return
     */
    @PostMapping(value = "im_open_login_svc/account_import")
    Map<String, Object> importAccount(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                      @RequestBody Map<String, String> requestBody);

    /**
     * 创建群
     * Owner_Account	    String	选填	群主 ID，自动添加到群成员中。如果不填，群没有群主。
     * Type	                String	必填	群组形态，包括 Public（公开群），Private（私密群），ChatRoom（聊天室），AVChatRoom（互动直播聊天室），BChatRoom（在线成员广播大群）。
     * GroupId	            String	选填	为了使得群组 ID 更加简单，便于记忆传播，腾讯云支持 App 在通过 REST API 创建群组时自定义群组 ID。详情参见：自定义群组ID。 最长 48 个字节，且前缀不能为 @TGS#
     * Name	                String	必填	群名称，最长 30 字节。
     * Introduction	        String	选填	群简介，最长 240 字节。
     * Notification	        String	选填	群公告，最长 300 字节。
     * FaceUrl	            String	选填	群头像 URL，最长 100 字节。
     * MaxMemberCount	    Integer	选填	最大群成员数量，缺省时的默认值：私有群是 200，公开群是 2000，聊天室是 10000，互动直播聊天室和在线成员广播大群无限制。
     * ApplyJoinOption	    String	选填	申请加群处理方式。包含 FreeAccess（自由加入），NeedPermission（需要验证），DisableApply（禁止加群），不填默认为 NeedPermission（需要验证）。
     * AppDefinedData	    Array	选填	群组维度的自定义字段，默认情况是没有的，需要开通，详情参见：自定义字段。
     * MemberList	        Array	选填	初始群成员列表，最多 500 个。成员信息字段详情参见：群成员资料。
     * AppMemberDefinedData	Array	选填	群成员维度的自定义字段，默认情况是没有的，需要开通，详情参见：自定义字段。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/create_group")
    Map<String, Object> createGroup(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                    @RequestBody Map<String, Object> requestBody);

    /**
     * 解散群
     * <p>
     * GroupId	String	必填	操作的群 ID。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/destroy_group")
    Map<String, Object> destroyGroup(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    /**
     * 修改群成员信息
     * <p>
     * GroupId	            String	必填	操作的群 ID。
     * Member_Account	    String	必填	要操作的群成员。
     * Role	                String	选填	成员身份，Admin/Member分别为设置/取消管理员。
     * MsgFlag	            String	选填	消息屏蔽类型。
     * NameCard	            String	选填	群名片（最大不超过50个字节）。
     * AppMemberDefinedData	Array	选填	群成员维度的自定义字段，默认情况是没有的，需要开通，详情参见：自定义字段。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/modify_group_member_info")
    Map<String, Object> modifyGroupMemberInfo(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                              @RequestBody Map<String, Object> requestBody);

    /**
     * 添加群成员 一次最多500
     * <p>
     * GroupId	        String	必填	操作的群 ID。
     * Silence	        Integer	选填	是否静默加人。0：非静默加人；1：静默加人。不填该字段默认为 0。
     * MemberList	    Array	必填	待添加的群成员数组。
     * Member_Account	String	必填	待添加的群成员帐号。
     * <p>
     * {
     * "GroupId": "@TGS#2J4SZEAEL",   // 要操作的群组（必填）
     * "Silence": 1,   // 是否静默加人（选填）
     * "MemberList": [  // 一次最多添加500个成员
     * {
     * "Member_Account": "tommy"  // 要添加的群成员ID（必填）
     * },
     * {
     * "Member_Account": "jared"
     * }]
     * }
     *
     * @return
     */
    @PostMapping(value = "/group_open_http_svc/add_group_member")
    Map<String, Object> addGroupMember(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                       @RequestBody Map<String, Object> requestBody);

    /**
     * 删除群成员 一次最多500
     * <p>
     * GroupId	            String	必填	操作的群 ID。
     * Silence	            Integer	选填	是否静默删人。0：非静默删人；1：静默删人。不填该字段默认为 0。
     * Reason	            String	选填	踢出用户原因。
     * MemberToDel_Account	Array	必填	待删除的群成员。
     * <p>
     * {
     * "GroupId": "@TGS#2J4SZEAEL",   //要操作的群组（必填）
     * "Reason": "kick reason",      //踢出用户原因（选填）
     * "MemberToDel_Account": [   // 要删除的群成员列表，最多500个
     * "tommy",
     * "jared"
     * ]
     * }
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/delete_group_member")
    Map<String, Object> deleteGroupMember(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                          @RequestBody Map<String, Object> requestBody);

    /**
     * 在群组中发送系统通知
     * <p>
     * https://cloud.tencent.com/document/product/269/1630
     * <p>
     * GroupId	            String	必填	向哪个群组发送系统通知。
     * ToMembers_Account	String	选填	接收者群成员列表，不填或为空表示全员下发。
     * Content	            String	必填	系统通知的内容。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/send_group_system_notification")
    Map<String, Object> sendGroupSystemNotification(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                                    @RequestBody Map<String, Object> requestBody);

    /**
     * 在群组中发送普通消息
     * <p>
     * https://cloud.tencent.com/document/product/269/1629
     * <p>
     * GroupId	String	                必填	向哪个群组发送消息。
     * Random	Integer	                必填	32 位随机数。如果 5分钟内两条消息的随机值相同，后一条消息将被当做重复消息而丢弃。
     * MsgPriority	String	            选填	消息的优先级。
     * MsgBody	Array	                必填	消息体，具体参见 消息格式描述。
     * From_Account	String	            选填	消息来源帐号，选填。如果不填写该字段，则默认消息的发送者为调用该接口时使用的 App 管理员 帐号。除此之外，App 亦可通过该字段“伪造”消息的发送者，从而实现一些特殊的功能需求。需要注意的是，如果指定该字段，必须要确保字段中的帐号是存在的。
     * OfflinePushInfo	Object	        选填	离线推送信息配置，具体可参考 消息格式描述。
     * ForbidCallbackControl	Array	选填	消息回调禁止开关，只对单条消息有效，ForbidBeforeSendMsgCallback 表示禁止发消息前回调，ForbidAfterSendMsgCallback 表示禁止发消息后回调。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/send_group_msg")
    Map<String, Object> sendGroupMsg(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    /**
     * 转让群
     * GroupId	        String	必填	要被转移的群组 ID。
     * NewOwner_Account	String	必填	新群主 ID。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "group_open_http_svc/change_group_owner")
    Map<String, Object> changeGroupOwner(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                         @RequestBody Map<String, String> requestBody);

    //-----------------好友-----------------------------------------------

    /**
     * 导入好友 可以单个或批量
     * <p>
     * https://cloud.tencent.com/document/product/269/8301
     * <p>
     * <p>
     * From_Account	    String	        必填	需要为该 Identifier 添加好友。
     * AddFriendItem	Array	        必填	好友结构体对象。
     * To_Account	    String	        必填	好友的 Identifier。
     * Remark	        String	        选填	From_Account 对 To_Account 的好友备注，详情可参见 标配好友字段。
     * RemarkTime	    Integer	        选填	From_Account 对 To_Account 的好友备注时间。
     * GroupName	    Array	        选填	From_Account 对 To_Account 的分组信息，详情可参见 标配好友字段。
     * AddSource	    String	        必填	加好友来源字段，详情可参见 标配好友字段。
     * AddWording	    String	        选填	From_Account 和 To_Account 形成好友关系时的附言信息，详情可参见 标配好友字段。
     * AddTime	        Integer	        选填	From_Account 和 To_Account 形成好友关系的时间。
     * CustomItem	    Array	        选填	From_Account 对 To_Account 的自定义表示对象信息数组，每一个对象都包含了 Tag 和 Value。
     * Tag	            String	        选填	From_Account 对 To_Account 的自定义标签，使用前需要联系云通讯团队申请一个 Tag。
     * Value	        String/Integer	选填	From_Account 对 To_Account 的自定义内容，可以是字符串，也可以是一个整数或者是一段 Buffer。
     * <p>
     * {
     * "From_Account": "id",
     * "AddFriendItem": [
     * {
     * "To_Account": "id1",
     * "Remark": "remark1",
     * "RemarkTime": 1420000001,
     * "GroupName": [
     * "朋友"
     * ],
     * "AddSource": "AddSource_Type_XXXXXXXX",
     * "AddWording": "I'm Test1",
     * "AddTime": 1420000001,
     * "CustomItem": [
     * {
     * "Tag": "Tag_SNS_1300000000_XXXX",
     * "Value": "Test"
     * },
     * {
     * "Tag": "Tag_SNS_1300000000_YYYY",
     * "Value": 0
     * }
     * ]
     * }
     * ]
     * }
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "sns/friend_import")
    Map<String, Object> friendImport(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    /**
     * 添加好友
     * <p>
     * https://cloud.tencent.com/document/product/269/1643
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "sns/friend_add")
    Map<String, Object> friendAdd(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                  @RequestBody Map<String, Object> requestBody);

    /**
     * 更新好友信息
     * <p>
     * https://cloud.tencent.com/document/product/269/12525
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "sns/friend_update")
    Map<String, Object> friendUpdate(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    /**
     * 删除好友 支持多个
     * <p>
     * https://cloud.tencent.com/document/product/269/1644
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "sns/friend_delete")
    Map<String, Object> friendDelete(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    /**
     * 拉取好友
     * <p>
     * https://cloud.tencent.com/document/product/269/1647
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "sns/friend_get_all")
    Map<String, Object> friendGetAll(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                     @RequestBody Map<String, Object> requestBody);

    //-----------------------------------------user-info------------------------------------

    /**
     * 拉取资料
     * <p>
     * https://cloud.tencent.com/document/product/269/1639
     * <p>
     * To_Account	Array	必填	需要拉取这些 Identifier 的资料：
     * 注意：每次拉取的用户数不得超过 100，避免因回包数据量太大以致回包失败。
     * TagList	    Array	必填	指定要拉取的资料字段的 Tag，支持的字段有：
     * 1、标配资料字段，详情可参见 标配资料字段；
     * 2、自定义资料字段，详情可参见 自定义资料字段。
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "profile/portrait_get")
    Map<String, Object> portraitGet(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                    @RequestBody Map<String, Object> requestBody);

    /**
     * 获取用户在线状态
     * <p>
     * https://cloud.tencent.com/document/product/269/2566
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "openim/querystate")
    Map<String, Object> querystate(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                   @RequestBody Map<String, Object> requestBody);

//---------------------------------notify---------------------------------------

    /**
     * 发送单聊消息
     * <p>
     * https://cloud.tencent.com/document/product/269/2282
     *
     * @param baseRequest
     * @param requestBody
     * @return
     */
    @PostMapping(value = "openim/sendmsg")
    Map<String, Object> sendmsg(@RequestParam("baseRequest") Map<String, Object> baseRequest,
                                @RequestBody Map<String, Object> requestBody);
}