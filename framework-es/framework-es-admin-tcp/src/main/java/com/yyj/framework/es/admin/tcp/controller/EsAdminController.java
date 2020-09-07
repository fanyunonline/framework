package com.yyj.framework.es.admin.tcp.controller;

import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.tcp.mapping.TypeMappingQo;
import com.yyj.framework.es.admin.tcp.model.DocQo;
import com.yyj.framework.es.admin.tcp.model.IndexAdminQo;
import com.yyj.framework.es.admin.tcp.service.EsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by yangyijun on 2018/12/3.
 */
@RestController
@Api(tags = "[elasticsearch管理模块-API]")
public class EsAdminController {

    @Autowired
    private EsAdminService esAdminService;

    @ApiOperation(value = "判断ES的index是否存在")
    @PostMapping("/existsIndex")
    public Response existsIndex(@RequestBody @Valid IndexAdminQo indexAdminQo) {
        return esAdminService.existsIndex(indexAdminQo);
    }

    @ApiOperation(value = "创建ES的index")
    @PostMapping("/createIndex")
    public Response createIndex(@RequestBody @Valid IndexMappingQo indexMappingQo) {
        return esAdminService.createIndex(indexMappingQo);
    }

    @ApiOperation(value = "删除ES的index")
    @PostMapping("/deleteIndex")
    public Response deleteIndex(@RequestBody @Valid IndexAdminQo indexAdminQo) {
        return esAdminService.deleteIndex(indexAdminQo);
    }

    @ApiOperation(value = "判断ES的type是否存在")
    @PostMapping("/existsType")
    public Response existsType(@RequestBody @Valid TypeMappingQo typeMappingQo) {
        return esAdminService.existsType(typeMappingQo);
    }

    @ApiOperation(value = "创建ES的type")
    @PostMapping("/createType")
    public Response createType(@RequestBody @Valid TypeMappingQo typeMappingQo) {
        return esAdminService.createType(typeMappingQo);
    }

    @ApiOperation(value = "清空ES的type中的数据")
    @PostMapping("/deleteType")
    public Response deleteType(@RequestBody @Valid TypeMappingQo typeMappingQo) {
        return esAdminService.deleteType(typeMappingQo);
    }

    @ApiOperation(value = "增删改ES数据")
    @PostMapping("/bulkCud")
    public Response bulkCud(@RequestBody @Valid DocQo docQo) {
        return esAdminService.bulkCud(docQo);
    }
}
