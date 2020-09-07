package com.yyj.framework.common.util.sort;

/**
 * Created by yangyijun on 2018/6/4.
 */
public enum Direction {
    /**
     * Ascending order.
     */
    ASC {
        @Override
        public String toString() {
            return "asc";
        }
    },
    /**
     * Descending order.
     */
    DESC {
        @Override
        public String toString() {
            return "desc";
        }
    }
}
