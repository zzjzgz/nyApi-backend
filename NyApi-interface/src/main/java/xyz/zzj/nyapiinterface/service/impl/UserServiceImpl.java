package xyz.zzj.nyapiinterface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zzj.nyapiinterface.domain.User;
import xyz.zzj.nyapiinterface.service.UserService;
import xyz.zzj.nyapiinterface.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author zengz
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-03-22 00:23:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




