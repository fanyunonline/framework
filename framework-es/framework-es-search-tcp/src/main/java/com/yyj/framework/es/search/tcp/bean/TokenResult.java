package com.yyj.framework.es.search.tcp.bean;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class TokenResult {
    private Set<String> keys = new LinkedHashSet<>();
    private Set<Token> tokens = new LinkedHashSet<>();

    public void addKey(String key) {
        if (key == null) {
            return;
        }
        this.keys.add(key);
    }

    public void addToken(Token token) {
        if (token == null) {
            return;
        }
        this.tokens.add(token);
    }
}
