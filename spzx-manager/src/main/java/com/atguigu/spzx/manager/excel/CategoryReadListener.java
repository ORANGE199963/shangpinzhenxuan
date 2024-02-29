package com.atguigu.spzx.manager.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryReadListener extends AnalysisEventListener<CategoryExcelVo> {

    @Autowired
    CategoryMapper categoryMapper;

    private static final int BATCH_COUNT = 5;

    List<Category> categoryList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(CategoryExcelVo categoryExcelVo, AnalysisContext analysisContext) {
        System.out.println("读取行:" + categoryExcelVo);
        Category category = new Category();
        BeanUtils.copyProperties(categoryExcelVo,category);
        categoryList.add(category);

        if (categoryList.size()==5){
            categoryMapper.addCategory(categoryList);
            categoryList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        System.out.println("所有行都读取完毕");
        categoryMapper.addCategory(categoryList);
        categoryList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    }
}
