package ru.example.discount.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.example.discount.entity.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends ListCrudRepository<Discount, Long> {

    @Query("from Discount d " +
            "where (d.productCategory.id = :product_category_id or d.productCategory is null) " +
            "and (d.clientCategory.id = :client_category_id or d.clientCategory is null) " +
            "order by " +
            "case " +
            "   when d.productCategory is not null and d.clientCategory is not null then 1 " +
            "   when d.productCategory is not null and d.clientCategory is null then 2 " +
            "   when d.productCategory is null and d.clientCategory is not null then 3 " +
            "   else 4 " +
            "end")
    List<Discount> findAllDiscountsForClientCategoryAndProductCategory(@Param("client_category_id") Long clientCategoryId,
                                                                       @Param("product_category_id") Long productCategoryId);

    @Query("from Discount d " +
            "where (d.productCategory.id = :product_category_id or d.productCategory is null) " +
            "and (d.clientCategory.id = :client_category_id or d.clientCategory is null) " +
            "order by " +
            "case " +
            "   when d.productCategory is not null and d.clientCategory is not null then 1 " +
            "   when d.productCategory is not null and d.clientCategory is null then 2 " +
            "   when d.productCategory is null and d.clientCategory is not null then 3 " +
            "   else 4 " +
            "end limit 1")
    Optional<Discount> findOneDiscountForClientCategoryAndProductCategory(@Param("client_category_id") Long clientCategoryId,
                                                                          @Param("product_category_id") Long productCategoryId);
}
