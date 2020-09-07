package com.yyj.framework.es.search.tcp.controller;

import com.yyj.framework.common.util.response.PageResponse;
import com.yyj.framework.es.search.tcp.model.FilterSearchQo;
import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import com.yyj.framework.es.search.tcp.model.IdSearchQo;
import com.yyj.framework.es.search.tcp.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Api(tags = "[elasticsearch搜索-API]")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

//    @Autowired
//    private EsAdminService esAdminService;

//    @ApiOperation(value = "判断es的index是否存在")
//    @GetMapping("/existsIndex")
//    public Response existsIndex(@RequestParam(value = "es的index名称") String index) {
//        return esAdminService.existsIndex(index);
//    }

    @ApiOperation(value = "条件过滤搜索")
    @PostMapping(path = "filterSearch")
    public PageResponse filterSearch(@RequestBody @Valid FilterSearchQo filterSearchQo) {
        return searchService.filterSearch(filterSearchQo);
    }

    @ApiOperation(value = "全文关键字搜索")
    @PostMapping(path = "fullTextSearch")
    public PageResponse fullTextSearch(@RequestBody @Valid FullTextSearchQo fullTextSearchQo) {
        return searchService.fullTextSearch(fullTextSearchQo);
    }

    @ApiOperation(value = "根据ID列表搜索")
    @PostMapping(path = "idSearch")
    public PageResponse idSearch(@RequestBody @Valid IdSearchQo idSearchQo) {
        return searchService.idSearch(idSearchQo);
    }

    @ApiOperation(value = "聚合搜索")
    @PostMapping(path = "aggsSearch")
    public PageResponse aggsSearch() {
        return PageResponse.success();
    }

}
