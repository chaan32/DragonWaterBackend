package com.dragonwater.backend.Web.Shop.Product.service.interf;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowCategoriesResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowMainAndSubCategoriesResDto;
import com.dragonwater.backend.Web.User.Member.dto.product.CategoryResDto;

import java.util.Queue;

public interface CategoryService {

    /**
     * 카테고리를 새로 만드는 메소드
     * @param mainCategories 연결 지어질 메인 카테고리
     * @param name 카테고리 이름
     * @return
     */
    ProductCategories addCategory(ProductMainCategories mainCategories, String name);

    /**
     * 메인 카테고리를 새로 만든느 메소드
     * @param name 카테고리 이름
     * @return
     */
    ProductMainCategories addMainCategory(String name);

    /**
     * 메인 카테고리를 가져오는 메소드
     * @param id 카테고리를 식별할 수 있는 id 값
     * @return
     */
    ProductMainCategories findMainCategory(Long id);

    /**
     * 모든 서브 카테고리를 가져오는 메소드
     * @return
     */
    Queue<CategoryResDto> findAllSubCategories();

    /**
     * 서브 카테고리를 가져오는 메소드
     * @param id 서브 카테고리를 식별할 수 있는 id 값
     * @return
     */
    Queue<CategoryResDto> findSubCategoriesById(Long id);

    /**
     * 모든 메인 카테고리를 가져오는 메소드
     * @return
     */
    Queue<CategoryResDto> findAllMainCategories();

    /**
     * 카테고리 구조를 담아서 가져오는 메소드
     * @return
     */
    Queue<ShowMainAndSubCategoriesResDto> findAllCategoriesShopV2();

    /**
     * 쇼핑몰 페이지에서 보여줄 모든 카테고리를 가져올 메소드
     * @return
     */
    Queue<ShowCategoriesResDto> findAllCategoriesShop();


    /**
     * 서브 카테고리를 가져오는 메소드
     * @param id 서브 카테고리를 식별할 수 있는 id 값
     * @return
     */
    ProductCategories getSubCategoryById(Long id);

    /**
     * 메인 카테고리를 가져오는 메소드
     * @param id 메인 카테고리를 식별할 수 있는 id 값
     * @return
     */
    ProductMainCategories getMainCategoryById(Long id);

    /**
     * 카테고리를 수정하는 메소드
     * @param id 카테고리를 식별할 수 있는 id 값
     * @param name 카테고리 이름
     * @param main 메인 카테고리인지 아닌지
     */
    void updateCategory(Long id, String name, boolean main);

    /**
     * 카테고리를 삭제하는 메소드
     * @param id 카테고리를 식별할 수 있는 id 값
     * @param main 메인 카테고리인지 아닌지
     */
    void deleteCategory(Long id,boolean main);

    /**
     * 모든 카테고리를 가져올 메소드
     * @return
     */
    Queue<CategoryResDto> findAllCategories();
}
