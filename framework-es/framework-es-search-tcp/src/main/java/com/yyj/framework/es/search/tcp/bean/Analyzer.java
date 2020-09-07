package com.yyj.framework.es.search.tcp.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by yangyijun on 2018/11/23.
 */
public final class Analyzer {

    private static final String REGEX_MD5 = "(^[a-zA-Z0-9]{32}$)";
    private static final String GROUP_DELIMITERS = " \t\n\r\f的跟与和";

    public static TokenResult parse(String text) {
        TokenResult result = new TokenResult();
        if (StringUtils.isBlank(text)) {
            return result;
        }

        StringTokenizer tokenizer = new StringTokenizer(text, GROUP_DELIMITERS);
        Set<String> set = new HashSet<>();
        while (tokenizer.hasMoreTokens()) {
            String term = tokenizer.nextToken();
            if (match(term, REGEX_MD5) && set.add(term)) {
                result.addKey(term);
                continue;
            }
            if (set.add(term)) {
                result.addToken(new Token(set.size(), term));
            }
        }
        return result;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private static boolean match(String term, String regex) {
        return Pattern.compile(regex).matcher(term).matches();
    }

}
