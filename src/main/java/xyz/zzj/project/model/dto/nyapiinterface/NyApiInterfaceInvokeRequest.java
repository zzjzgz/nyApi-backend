package xyz.zzj.project.model.dto.nyapiinterface;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class NyApiInterfaceInvokeRequest extends PageRequest implements Serializable {

    /**
     * 主键，接口id
     */
    private Long id;

    /**
     * 用户传递请求参数
     */
    private String userRequestParams;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}