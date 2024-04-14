package xyz.zzj.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import xyz.zzj.common.model.entity.User;
import xyz.zzj.common.service.InnerUserService;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.mapper.UserMapper;

import javax.annotation.Resource;

/**
 * @BelongsPackage: xyz.zzj.project.service.impl
 * @ClassName: InnerUserServiceImpl
 * @Author: zengz
 * @CreateTime: 2024/4/9 21:33
 * @Description: 获取用户信息的实现类
 * @Version: 1.0
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据accessKey和secretKey获取用户信息
     * @param accessKey access密钥
     * @return 用户信息
     */
    @Override
    public User getUserInfo(String accessKey) {
        // 参数校验，accessKey和secretKey不能为空
        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"accessKey不能为空");
        }
        // 根据accessKey查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey,accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
