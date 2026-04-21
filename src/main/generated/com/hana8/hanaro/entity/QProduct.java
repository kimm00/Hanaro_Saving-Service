package com.hana8.hanaro.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1848746138L;

    public static final QProduct product = new QProduct("product");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Double> cancelRate = createNumber("cancelRate", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ProductImage, QProductImage> images = this.<ProductImage, QProductImage>createList("images", ProductImage.class, QProductImage.class, PathInits.DIRECT2);

    public final NumberPath<Double> interestRate = createNumber("interestRate", Double.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> paymentAmount = createNumber("paymentAmount", Long.class);

    public final EnumPath<com.hana8.hanaro.common.enums.PaymentCycle> paymentCycle = createEnum("paymentCycle", com.hana8.hanaro.common.enums.PaymentCycle.class);

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final EnumPath<com.hana8.hanaro.common.enums.ProductType> productType = createEnum("productType", com.hana8.hanaro.common.enums.ProductType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

