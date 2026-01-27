package com.kazibu.auth.service;

import java.util.Map;

public interface WeChatService {
  /**
   * 通过code获取微信openid和session_key
   * 
   * @param code 微信登录凭证
   * @return 包含openid和session_key的Map
   */
  Map<String, Object> getWeChatSession(String code);

  /**
   * 验证微信数据是否被篡改
   * 
   * @param rawData 原始数据
   * @param signature 签名
   * @param sessionKey session_key
   * @return 是否验证通过
   */
  boolean validateWeChatData(String rawData, String signature, String sessionKey);
}
