package luke.shopbackend.productCategory.service;

import luke.shopbackend.productCategory.model.ProductCategory;

import java.util.Set;

public interface ProductCategoryService {

    Set<ProductCategory> getCategories();
}
