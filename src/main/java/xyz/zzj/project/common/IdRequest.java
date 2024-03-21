package xyz.zzj.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * id请求
 *
 * @author zeng
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}