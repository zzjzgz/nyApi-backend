package xyz.zzj.nyapiinterface.controller;

import org.springframework.web.bind.annotation.*;
import xyz.zzj.nyapiinterface.model.User;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/")
    public String getName(String name){

        return "GET 你的名字是：" + name;
    }
    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "POST 你的名字是：" + name;
    }
    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){
        String accessKey = request.getHeader("accessKey");
        String secretKey = request.getHeader("secretKey");
        if (!accessKey.equals("nianyan") || !secretKey.equals("qwertyuiop")){
            throw new RuntimeException("无权限");
        }
        return "post 你的名字是：" + user.getUserName();
    }

}
