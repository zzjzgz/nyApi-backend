package xyz.zzj.common.service;




import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zzj.common.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author zeng
 */
public interface InnerUserService{

    //1. 通过accessKey查出用户信息
    User getUserInfo(String accessKey);
}
