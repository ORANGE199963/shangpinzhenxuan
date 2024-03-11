package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Operation(summary = "修改商品")
    @PostMapping("updateProduct")
    public Result updateProduct(@RequestBody Product product){
        productService.updateProduct(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("updateSkuStatus/{skuId}/{status}")
    public Result updateSkuStatus(@PathVariable Long skuId,@PathVariable Integer status){
        productService.updateSkuStatus(skuId,status);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("updateStatus/{spuId}/{status}")
    public Result updateStatus(@PathVariable Long spuId,@PathVariable Integer status){
        productService.updateStatus(spuId,status);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("updateAuditStatus/{productId}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long productId,@PathVariable Integer auditStatus){
        productService.updateAuditStatus(productId,auditStatus);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @DeleteMapping("deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        productService.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("getByProductId/{productId}")
    public Result getByProductId(@PathVariable Long productId){
        Product product = productService.getByProductId(productId);
        return Result.build(product, ResultCodeEnum.SUCCESS);
    }


    @PostMapping("addProduct")
    public Result addProduct(@RequestBody Product product){
        productService.addProduct(product);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
    @GetMapping("findAllProductUnit")
    public Result findAllProductUnit(){
        List<ProductUnit> list = productService.findAllProductUnit();
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, ProductDto productDto){
        PageInfo pageInfo = productService.findByPage(pageNum,pageSize,productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }
}
