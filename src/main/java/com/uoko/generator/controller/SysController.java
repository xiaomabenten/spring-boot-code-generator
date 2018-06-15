package com.uoko.generator.controller;

import com.alibaba.fastjson.JSON;
import com.uoko.generator.service.SysService;
import com.uoko.generator.util.GeneratorUtil;
import com.uoko.generator.util.PageUtils;
import com.uoko.generator.util.Query;
import com.uoko.generator.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自动生成代码 controller
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/6/13
 */
@Api("自动生成代码接口")
@RestController
@RequestMapping("sys/generator")
public class SysController {

    @Autowired
    private SysService sysService;

    /**
     * 获取当前数据的表名
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取数据表列表", responseContainer = "Map")
    public Response getList(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<Map<String, Object>> list = sysService.queryList(query);
        int total = sysService.queryTotal(query);
        PageUtils pageUtils = new PageUtils(list, total, query.getLimit(), query.getPage());
        return Response.ok().put("page", pageUtils);
    }


    /**
     * 生成remote代码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + GeneratorUtil.getConfig().getString("projectName") + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 生成前段代码
     */
    public void frontCode() {

    }
}
