package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductSkuMapper productSkuMapper;
    @Autowired
    ProductDetailsMapper productDetailsMapper;
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, ProductDto productDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> list = productMapper.selectList(productDto);
        return new PageInfo(list);
    }

    @Override
    public List<ProductUnit> findAllProductUnit() {
        return productMapper.findAllProductUnit();
    }


        @Override
        public void addProduct(Product product) {

            //1、添加spu，并返回id主键
            productMapper.addProduct(product);

            Long productId = product.getId();//当前spu的id

            //2、添加sku列表
            List<ProductSku> productSkuList = product.getProductSkuList();
            for (int i = 0; productSkuList!=null && i < productSkuList.size(); i++) {
                ProductSku productSku = productSkuList.get(i);

                productSku.setSkuCode(productId + "_" + i);
                productSku.setSkuName(product.getName() + " " + productSku.getSkuSpec());//spu名称 + 当前sku的规格组合
                productSku.setProductId(productId);

                productSkuMapper.addProductSku(productSku);
            }

            //3、添加details
            String detailsImageUrls = product.getDetailsImageUrls();// url1,url2,url3...
            ProductDetails productDetails = new ProductDetails();
            productDetails.setProductId(productId);
            productDetails.setImageUrls(detailsImageUrls);
            productDetailsMapper.insertProductDetails(productDetails);

        }

    @Override
    public Product getByProductId(Long productId) {
        Product product = productMapper.getById(productId);

        //2、根据spuid，查询sku列表，赋值给product对象的productSkuList属性
        List<ProductSku> skuList = productSkuMapper.getByProductId(productId);
        product.setProductSkuList(skuList);

        //3、根据spuid，查询product_details，获取他的image_urls属性赋值给product对象的detailsImageUrls
        ProductDetails productDetails = productDetailsMapper.getByProductId(productId);
        product.setDetailsImageUrls(productDetails.getImageUrls());

        return product;
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
        productSkuMapper.deleteByProductId(id);
        productDetailsMapper.deleteByProductId(id);
    }

    @Override
    public void updateAuditStatus(Long productId, Integer auditStatus) {
        productMapper.updateAuditStatus(productId,auditStatus);
    }

    @Override
    public void updateStatus(Long spuId, Integer status) {
        Product product = productMapper.getById(spuId);
        if(product.getAuditStatus()!=1){
            throw new GuiguException(400,"该商品审核未通过,暂不允许进行上下架操作");
        }
        if(status!=1 && status!=-1){
            throw new GuiguException(400,"上下架状态的取值范围不正确");
        }
        productMapper.updateStatus(spuId,status);
        productSkuMapper.updateStatus(spuId,status);
    }

    @Override
    public void updateSkuStatus(Long skuId, Integer status) {
        //单独对sku进行上下架
        //1、校验spu的审核状态，如果auditStatus！= 1 ，则不允许进行sku的上下架

        //根据sku的id查询sku对象
        ProductSku productSku = productSkuMapper.getById(skuId);
        //从sku对象中获取spu的id
        Long productId = productSku.getProductId();
        //根据spu的id查询spu对象
        Product product = productMapper.getById(productId);
        //判断spu的auditStatus是否为1，不等于1表示未审核通过，则不允许对sku进行上下级
        if(product.getAuditStatus()!=1){
            throw new GuiguException(400,"当前sku对应的spu未审核通过");
        }


        //补充的判断：当前spu的status是否等于1（spu必须得上架，sku才能单独上下架）
        if(product.getStatus()!=1){
            throw new GuiguException(400,"当前spu必须先上架之后，才能对该sku进行上下架操作！");
        }

        //或者SELECT b.* FROM `product_sku` a
        //INNER JOIN `product` b ON a.product_id = b.id
        //WHERE a.id = 1    根据sku的id查询对应的spu

        //2、执行sql , UPDATE `product_sku` SET STATUS = #{status} where id = #{skuId}
        //根据sku的id修改status
        productSkuMapper.updateStatusBySkuId(skuId,status);

    }

    @Override
    public void updateProduct(Product product) {
        productMapper.updateById(product);//更新spu

        for (ProductSku productSku : product.getProductSkuList()) {
            productSku.setSkuName(product.getName() + " " + productSku.getSkuSpec());
            productSkuMapper.updateById(productSku);//更新sku
        }

        String detailsImageUrls = product.getDetailsImageUrls();//详情图
        productDetailsMapper.updateByProductId(product.getId(),detailsImageUrls);
    }
    }
