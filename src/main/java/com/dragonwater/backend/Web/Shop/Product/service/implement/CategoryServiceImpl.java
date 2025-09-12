package com.dragonwater.backend.Web.Shop.Product.service.implement;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.ProductCategoryNotFoundException;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowCategoriesResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowMainAndSubCategoriesResDto;
import com.dragonwater.backend.Web.Shop.Product.repository.CategoryRepository;
import com.dragonwater.backend.Web.Shop.Product.repository.MainCategoryRepository;
import com.dragonwater.backend.Web.Shop.Product.service.interf.CategoryService;
import com.dragonwater.backend.Web.User.Member.dto.product.CategoryResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MainCategoryRepository mainCategoryRepository;

    @Override
    @Transactional
    public ProductCategories addCategory(ProductMainCategories mainCategories, String name) {
        try {
            ProductCategories save = categoryRepository.save(new ProductCategories(mainCategories, name));
            return save;
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    @Transactional
    public ProductMainCategories addMainCategory(String name) {
        try {
            ProductMainCategories save = mainCategoryRepository.save(new ProductMainCategories(name));
            return save;
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    public ProductMainCategories findMainCategory(Long id) {
        return mainCategoryRepository.findById(id).orElseThrow(
                ()-> new ProductCategoryNotFoundException());
    }


    @Override
    public Queue<CategoryResDto> findAllSubCategories() {
        List<ProductCategories> all = categoryRepository.findAll();

        Queue<CategoryResDto> dtos = new LinkedList<>();
        for (ProductCategories productCategories : all) {
            dtos.add(CategoryResDto.of(productCategories));
        }
        return dtos;
    }

    @Override
    public Queue<CategoryResDto> findSubCategoriesById(Long id) {
        List<ProductCategories> all = categoryRepository.findByMainCategoryId(id);

        Queue<CategoryResDto> dtos = new LinkedList<>();
        for (ProductCategories productCategories : all) {
            dtos.add(CategoryResDto.of(productCategories));
        }
        return dtos;
    }

    @Override
    public Queue<CategoryResDto> findAllMainCategories() {
        List<ProductMainCategories> all = mainCategoryRepository.findAll();

        Queue<CategoryResDto> dtos = new LinkedList<>();
        for (ProductMainCategories productCategories : all) {
            dtos.add(CategoryResDto.of(productCategories));
        }
        return dtos;
    }


    @Override
    public Queue<ShowMainAndSubCategoriesResDto> findAllCategoriesShopV2() {

        Queue<ShowMainAndSubCategoriesResDto> dtos = new LinkedList<>();
        for (ProductMainCategories productCategories : mainCategoryRepository.findAll()) {
            dtos.add(ShowMainAndSubCategoriesResDto.of(productCategories));
        }
        return dtos;
    }

    @Override
    public Queue<ShowCategoriesResDto> findAllCategoriesShop() {

        Queue<ShowCategoriesResDto> dtos = new LinkedList<>();
        for (ProductCategories productCategories : categoryRepository.findAll()) {
            dtos.add(ShowCategoriesResDto.of(productCategories));
        }
        return dtos;
    }



    @Override
    public ProductCategories getSubCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ProductCategoryNotFoundException());
    }

    @Override
    public ProductMainCategories getMainCategoryById(Long id) {
        return mainCategoryRepository.findById(id).orElseThrow(()-> new ProductCategoryNotFoundException());
    }

    @Override
    @Transactional
    public void updateCategory(Long id, String name, boolean main) {
        if (!main){
            ProductCategories subC = getSubCategoryById(id);
            subC.editCategoryName(name);
        }
        else {
            ProductMainCategories mainC = getMainCategoryById(id);
            mainC.editCategoryName(name);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id,boolean main) {
        if (!main) {
            categoryRepository.deleteById(id);
        } else {
            mainCategoryRepository.deleteById(id);
        }
    }

    @Override
    public Queue<CategoryResDto> findAllCategories() {
        Queue<CategoryResDto> categoryResDtos = new LinkedList<>();
        for (ProductMainCategories productMainCategories : mainCategoryRepository.findAll()) {
            categoryResDtos.add(CategoryResDto.of(productMainCategories));
        }
        return categoryResDtos;
    }
}
