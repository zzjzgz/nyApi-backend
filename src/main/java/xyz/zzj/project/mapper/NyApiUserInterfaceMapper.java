package xyz.zzj.project.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.zzj.common.model.entity.NyApiUserInterface;

import java.util.List;

/**
* @author zengz
* @description 针对表【ny_api_user_interface(用户接口关系表)】的数据库操作Mapper
* @createDate 2024-03-24 16:12:44
* @Entity xyz.zzj.project.model.entity.NyApiUserInterface
*/
public interface NyApiUserInterfaceMapper extends BaseMapper<NyApiUserInterface> {
//    select interfaceId,sum(totalNum) as num
//    from ny_api_user_interface
//    group by interfaceId order by totalNum desc
//    limit 5;
    List<NyApiUserInterface> listTopInvokeInterfaceInfo(int limit);
}




