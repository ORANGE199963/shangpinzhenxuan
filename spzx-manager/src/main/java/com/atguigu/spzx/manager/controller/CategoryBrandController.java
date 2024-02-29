package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.CategoryBrandService;
import com.atguigu.spzx.model.dto.product.CategoryBrandDto;
import com.atguigu.spzx.model.entity.product.CategoryBrand;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/categoryBrand")
public class CategoryBrandController {
    @Autowired
    CategoryBrandService categoryBrandService;

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        categoryBrandService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
    @PutMapping("updateById")
    public Result updateById(@RequestBody CategoryBrand categoryBrand) {
        categoryBrandService.updateById(categoryBrand);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "根据第三级分类id，返回id集合（包含3个分类id）")
    @GetMapping("getIdsByCategory3Id/{category3Id}")
    public Result getIdsByCategory3Id(@PathVariable Long category3Id){
        List<Long> idList = categoryBrandService.getIdsByCategory3Id(category3Id);
        return Result.build(idList,ResultCodeEnum.SUCCESS);
    }
    @PostMapping("addCategoryBrand")
    public Result addCategoryBrand(@RequestBody CategoryBrand categoryBrand){
        categoryBrandService.addCategoryBrand(categoryBrand);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "查询品牌分类列表")
    @PostMapping("{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize,@RequestBody CategoryBrandDto categoryBrandDto){
        PageInfo pageInfo = categoryBrandService.findByPage(pageNum,pageSize,categoryBrandDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
