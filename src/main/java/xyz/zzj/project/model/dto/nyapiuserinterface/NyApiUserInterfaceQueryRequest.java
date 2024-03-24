package xyz.zzj.project.model.dto.nyapiuserinterface;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.zzj.project.common.PageRequest;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author zeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NyApiUserInterfaceQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调用人id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 调用状态，0-正常，1-禁用
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}