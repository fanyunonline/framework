package com.yyj.framework.es.search.tcp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private int sequence;
    private String term;
}
