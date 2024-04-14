package xyz.zzj.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import xyz.zzj.common.model.entity.NyApiUserInterface;
import xyz.zzj.common.service.InnerNyApiUserInterfaceService;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.mapper.NyApiUserInterfaceMapper;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static xyz.zzj.project.constant.userInterfaceConstant.UPDATE_NUM;

/**
 * @BelongsPackage: xyz.zzj.project.service.impl
 * @ClassName: InnerNyApiUserInterfaceServiceImpl
 * @Author: zengz
 * @CreateTime: 2024/4/9 21:35
 * @Description:  统计接口调用次数
 * @Version: 1.0
 */
@DubboService
public class InnerNyApiUserInterfaceServiceImpl implements InnerNyApiUserInterfaceService {

    @Resource
    private NyApiUserInterfaceMapper userInterfaceMapper;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 调用接口计数
     * @param interfaceInfoId 接口信息ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //判断
        if (interfaceInfoId <= 0 || userId  <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //加分布式锁
        RLock lock = redissonClient.getLock(UPDATE_NUM + userId);
        try {
            int i = 0;
            while (true){
                //重试10次放弃
                if (i > 10){
                    return false;
                }
                // 尝试获取锁，并进行相关数据库操作
                if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                    //查询接口信息
                    LambdaUpdateWrapper<NyApiUserInterface> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(NyApiUserInterface::getInterfaceId,interfaceInfoId);
                    lambdaUpdateWrapper.eq(NyApiUserInterface::getUserId,userId);
                    NyApiUserInterface nyApiUserInterface = userInterfaceMapper.selectOne(lambdaUpdateWrapper);
                    //判断接口信息是否存在
                    if (nyApiUserInterface == null){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口信息不存在");
                    }
                    UpdateWrapper<NyApiUserInterface> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("interfaceId",interfaceInfoId);
                    updateWrapper.eq("userId",userId);
                    updateWrapper.gt("leftNum",0);
                    updateWrapper.setSql("leftNum = leftNum -1,totalNum = totalNum + 1");
                    return userInterfaceMapper.update(nyApiUserInterface,updateWrapper) > 0;
                }
                i++;
            }
        } catch (Exception e) {
            return false;
        } finally {
            //释放自己的锁
            if (lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
