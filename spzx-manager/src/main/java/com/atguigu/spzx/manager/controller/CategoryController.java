package com.atguigu.spzx.manager.controller;

import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "商品分类管理")
@RestController
@RequestMapping("/admin/product/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Operation(summary = "导入接口")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        categoryService.importData(file);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("全部商品分类", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");


        List<Category> categoryList = categoryService.findAll();
        List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
        categoryList.forEach(category -> {
            CategoryExcelVo excelVo = new CategoryExcelVo();
            BeanUtils.copyProperties(category,excelVo);

            categoryExcelVoList.add(excelVo);
        });
        EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                .sheet("全部商品分类")
                .doWrite(categoryExcelVoList);
    }
    @Operation(summary = "查询分类列表,并且统计该分类是否存在下级")
    @GetMapping("findByParentId/{parentId}")
    public Result findByParentId(@PathVariable Long parentId){
        List<Category> list= categoryService.findByParentId(parentId);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
