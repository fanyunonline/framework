package com.yyj.framework.common.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangyijun on 2018/12/2.
 */
@Data
@MappedSuperclass
public class BasePo implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", insertable = false)
    protected Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_date", insertable = false, updatable = false)
    protected Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_date", insertable = false, updatable = false)
    protected Date updateDate;

    @Column(name = "creator", updatable = false)
    protected String creator;

    @Column(name = "modifier")
    protected String modifier;
}

