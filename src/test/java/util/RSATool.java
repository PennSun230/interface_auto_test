package util;

import com.lemon.encryption.RSAManager;
import data.EnvironmentConstant;

import java.util.Random;

/**
 * @author penn
 * @date 2021/5/3 --- 9:49
 * 柠檬班学习
 */
public class RSATool {

    public static void signRequest() throws Exception {
        //timestamp参数
        long timestamp = System.currentTimeMillis() / 1000;
        //sign参数
        //1、取token的前面50位
        String token = (String) EnvironmentConstant.environmentData.get("token");
        String preStr = token.substring(0, 50);
        //2、取到的结果拼接上timestamp
        String str = preStr + timestamp;
        //3、通过RSA加密算法对拼接的结果进行加密,得到sign签名
        String sign = RSAManager.encryptWithBase64(str);
        //保存到环境变量中
        EnvironmentConstant.environmentData.put("timestamp", timestamp);
        EnvironmentConstant.environmentData.put("sign", sign);
    }
}
