//package com.yyj.framework.es.search.service;
//
//import com.yyj.framework.common.util.response.Response;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * Created by yangyijun on 2018/12/3.
// */
//@FeignClient(value = "es-admin")
//public interface EsAdminService {
//    @GetMapping("/es/admin/existsIndex")
//    Response existsIndex(@RequestParam(value = "es的index名称") String index);
//}
