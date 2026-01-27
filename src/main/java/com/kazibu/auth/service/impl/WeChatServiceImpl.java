package com.kazibu.auth.service.impl;

import com.kazibu.auth.service.WeChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeChatServiceImpl implements WeChatService {

  @Value("${wechat.miniapp.appid}")
  private String appId;

  @Value("${wechat.miniapp.secret}")
  private String appSecret;

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";

  @Override
  public Map<String, Object> getWeChatSession(String code) {
    try {
      // 构建请求URL
      String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
          WECHAT_API_URL, appId, appSecret, code);

      // 调用微信API
      String responseStr = restTemplate.getForObject(url, String.class);
      if (responseStr == null) {
        throw new RuntimeException("微信API调用失败");
      }
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> response = objectMapper.readValue(responseStr, new TypeReference<Map<String, Object>>() {});

      if (response == null) {
        throw new RuntimeException("微信API调用失败");
      }

      // 检查是否有错误
      if (response.containsKey("errcode")) {
        Integer errcode = (Integer) response.get("errcode");
        String errmsg = (String) response.get("errmsg");
        throw new RuntimeException("微信API错误: " + errcode + " - " + errmsg);
      }

      return response;
    } catch (Exception e) {
      throw new RuntimeException("获取微信session失败: " + e.getMessage(), e);
    }
  }

  @Override
  public boolean validateWeChatData(String rawData, String signature, String sessionKey) {
    try {
      // 使用SHA1算法计算签名
      String data = rawData + sessionKey;
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
      
      // 转换为十六进制字符串
      StringBuilder hexString = new StringBuilder();
      for (byte b : digest) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      
      String calculatedSignature = hexString.toString();
      return calculatedSignature.equals(signature);
    } catch (Exception e) {
      return false;
    }
  }
}
