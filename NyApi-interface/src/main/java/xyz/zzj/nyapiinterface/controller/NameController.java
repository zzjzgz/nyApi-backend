package xyz.zzj.nyapiinterface.controller;

import org.springframework.web.bind.annotation.*;
import xyz.zzj.nyapiclientsdk.model.User;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @BelongsPackage: xyz.zzj.nyapiinterface.controller
 * @ClassName: NameController
 * @Author: zengz
 * @CreateTime: 2024/2/23 19:04
 * @Description: 名称API
 * @Version: 1.0
 */

@RestController()
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getName(String name,HttpServletRequest request){
        System.out.println(request.getHeader("zzj"));
        return "GET 你的名字是：" + name;
    }
    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "POST 你的名字是：" + name;
    }
    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String body = request.getHeader("body");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        //查询用户的accessKey值和secretKey值，在进行比较
//        //判断accessKey的用户是否存在
////        QueryWrapper<xyz.zzj.nyapiinterface.domain.User> queryWrapper = new QueryWrapper<>();
////        queryWrapper.eq("accessKey",accessKey);
////        xyz.zzj.nyapiinterface.domain.User currentUser = userService.getOne(queryWrapper);
////        if (currentUser == null) {
////            throw new RuntimeException("无权限");
////        }
//        //对时间进行校验，时间不超过当前时间的5分钟
//        long now = System.currentTimeMillis() / 1000;
//        long longValue = Optional.of(Long.valueOf(timestamp)).orElse(0L);
//        if ((now - longValue)/60 > 5){
//            throw new RuntimeException("无权限");
//        }
//        //对随机数进行校验
//        if (Long.parseLong(nonce) > 10000){
//            throw new RuntimeException("无权限");
//        }
//        //使用的是数据库查询出来的secretKey
////        String serverSign = SignUtils.getSign(body, currentUser.getSecretKey());
////        if (!sign.equals(serverSign)){
////            throw new RuntimeException("无权限");
////        }
        return user.getUsername();
    }

}
