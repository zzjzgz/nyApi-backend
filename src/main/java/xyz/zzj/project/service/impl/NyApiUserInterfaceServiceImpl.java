package xyz.zzj.project.service.impl;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import xyz.zzj.common.model.entity.NyApiUserInterface;
import xyz.zzj.project.common.ErrorCode;
import xyz.zzj.project.exception.BusinessException;
import xyz.zzj.project.mapper.NyApiUserInterfaceMapper;
import xyz.zzj.project.service.NyApiUserInterfaceService;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static xyz.zzj.project.constant.userInterfaceConstant.UPDATE_NUM;

/**
* @author zengz
* @description 针对表【ny_api_user_interface(用户接口关系表)】的数据库操作Service实现
* @createDate 2024-03-24 16:12:44
*/
@Service
public class NyApiUserInterfaceServiceImpl extends ServiceImpl<NyApiUserInterfaceMapper, NyApiUserInterface> implements NyApiUserInterfaceService {

    @Resource
    private RedissonClient redissonClient;


    @Override
    public void validNyApiUserInterface(xyz.zzj.common.model.entity.NyApiUserInterface nyApiUserInterface, boolean add) {
        if (nyApiUserInterface == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = nyApiUserInterface.getId();
        Long userId = nyApiUserInterface.getUserId();
        Long interfaceId = nyApiUserInterface.getInterfaceId();
        Integer leftNum = nyApiUserInterface.getLeftNum();


        if (add){
            //当有参数为空时，返回异常
            if (ObjectUtils.anyNull(id,userId,interfaceId)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (leftNum < 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余次数不能小于0");
            }
        }
    }

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
                    UpdateWrapper<NyApiUserInterface> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("interfaceId",interfaceInfoId);
                    updateWrapper.eq("userId",userId);
                    updateWrapper.gt("leftNum",0);
                    updateWrapper.setSql("leftNum = leftNum -1,totalNum = totalNum + 1");
                    return this.update(updateWrapper);
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




