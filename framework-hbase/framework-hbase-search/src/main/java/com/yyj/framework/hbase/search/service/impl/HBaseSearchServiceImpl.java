package com.yyj.framework.hbase.search.service.impl;

import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.hbase.search.dao.HBaseSearchDao;
import com.yyj.framework.hbase.search.model.HBaseSearchQo;
import com.yyj.framework.hbase.search.service.HBaseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by yangyijun on 2018/11/12.
 */
@Service
public class HBaseSearchServiceImpl implements HBaseSearchService {

    @Autowired
    private HBaseSearchDao hBaseSearchDao;

    @Override
    public Response<List<Map<String, String>>> search(HBaseSearchQo hBaseSearchQo) {
        return Response.success(hBaseSearchDao.getByRowKeys(hBaseSearchQo));
    }
}
