package xyz.zzj.project.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.zzj.common.model.entity.NyApiInterface;

/**
 * @BelongsPackage: xyz.zzj.project.model.vo
 * @ClassName: interfaceInfoVo
 * @Author: zengz
 * @CreateTime: 2024/4/14 16:23
 * @Description: TODO 描述类的功能
 * @Version: 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVo extends NyApiInterface {

    private static final long serialVersionUID = 7725308940818478211L;
    /**
     * 接口调用总数
     */
    private Integer totalNum;

}
