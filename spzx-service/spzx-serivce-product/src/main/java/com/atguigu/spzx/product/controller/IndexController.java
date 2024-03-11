package com.atguigu.spzx.product.controller;

import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.IndexVo;
import com.atguigu.spzx.product.service.CategoryService;
import com.atguigu.spzx.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin
@Tag(name = "首页接口管理")
@RestController
@RequestMapping(value = "/api/product/index")
public class IndexController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public Result index(){
        List<ProductSku> productSkuList = productService.findIndexSkuList();
        List<Category> oneCategoryList = categoryService.findByParentId(0L);

        IndexVo indexVo = new IndexVo();
        indexVo.setProductSkuList(productSkuList);
        indexVo.setCategoryList(oneCategoryList);

        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }

}
