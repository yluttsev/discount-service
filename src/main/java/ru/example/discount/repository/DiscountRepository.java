package ru.example.discount.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.example.discount.entity.Discount;
import ru.example.discount.entity.enums.DiscountType;

/**
 * Репозиторий для получения скидок
 */
public interface DiscountRepository extends ListCrudRepository<Discount, Long> {

    /**
     * Получение списка скидок для данных категорий клиента и продукта.<br>
     * Сортирует список скидок от более специфичной скидки к более общей.
     *
     * @param clientCategoryId  ID категории клиента
     * @param productCategoryId ID категории продукта
     * @param discountType      тип скидки {@link DiscountType}
     * @param pageable          объект {@link Pageable}
     * @return {@link Page} со скидками
     */
    @Query("from Discount d " +
            "where (d.productCategory.id = :product_category_id or d.productCategory is null) " +
            "and (d.clientCategory.id = :client_category_id or d.clientCategory is null) " +
            "and d.discountType = :discount_type " +
            "order by " +
            "case " +
            "   when d.productCategory is not null and d.clientCategory is not null then 1 " +
            "   when d.productCategory is not null and d.clientCategory is null then 2 " +
            "   when d.productCategory is null and d.clientCategory is not null then 3 " +
            "   else 4 " +
            "end")
    @EntityGraph(attributePaths = {"productCategory", "clientCategory"})
    Page<Discount> findByClientCategoryAndProductCategory(@Param("client_category_id") Long clientCategoryId,
                                                          @Param("product_category_id") Long productCategoryId,
                                                          @Param("discount_type") DiscountType discountType,
                                                          Pageable pageable);
}
