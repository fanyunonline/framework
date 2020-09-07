package com.yyj.framework.hbase.search.controller;

import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.hbase.search.model.HBaseSearchQo;
import com.yyj.framework.hbase.search.service.HBaseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/11/10.
 */
@Api(description = "[HBase搜索API]")
@RestController
@RequestMapping("/api")
public class HBaseSearchController {

    @Autowired
    private HBaseSearchService hBaseSearchService;

    @ApiOperation(value = "搜索通用接口")
    @PostMapping(value = "")
    public Response<List<Map<String, String>>> search(@RequestBody @Valid HBaseSearchQo hBaseSearchQo) {
        return hBaseSearchService.search(hBaseSearchQo);
    }
}