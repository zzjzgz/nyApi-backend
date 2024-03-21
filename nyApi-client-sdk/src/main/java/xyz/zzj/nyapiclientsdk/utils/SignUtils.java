package xyz.zzj.nyapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @BelongsPackage: xyz.zzj.nyapiinterface.utils
 * @ClassName: SignUtils
 * @Author: zengz
 * @CreateTime: 2024/3/5 9:57
 * @Description: TODO 描述类的功能
 * @Version: 1.0
 */
public class SignUtils {
    /**
     * 生成签名的加密算法
     * @param body
     * @param secretKey
     * @return
     */
    public static String getSign(String body,String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }
}
