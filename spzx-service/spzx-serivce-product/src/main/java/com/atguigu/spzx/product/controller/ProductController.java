package com.atguigu.spzx.product.controller;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "商品管理")
@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("getProductId/{skuId}")
    public ProductSku getProductId(@PathVariable Long skuId){
        return productService.getBySkuId(skuId);
    }

    @GetMapping("item/{skuId}")
    public Result findBySkuId(@PathVariable Long skuId){
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, ProductSkuDto productSkuDto){
        PageInfo pageInfo = productService.findByPage(pageNum,pageSize,productSkuDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @GetMapping("/brand/findAll")
    public Result findAllBrand(){
        List<Brand> list = productService.findAllBrand();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }
}
