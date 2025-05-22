package com.example.plusproject.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 246904755L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final com.example.plusproject.common.entity.QBaseEntity _super = new com.example.plusproject.common.entity.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> couponEndDay = createDateTime("couponEndDay", java.time.LocalDateTime.class);

    public final NumberPath<Long> couponQuantity = createNumber("couponQuantity", Long.class);

    public final DateTimePath<java.time.LocalDateTime> couponStartDay = createDateTime("couponStartDay", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> discountPrice = createNumber("discountPrice", Integer.class);

    public final EnumPath<com.example.plusproject.domain.coupon.enums.DiscountType> discountType = createEnum("discountType", com.example.plusproject.domain.coupon.enums.DiscountType.class);

    public final BooleanPath duplicatePossible = createBoolean("duplicatePossible");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxDiscountPrice = createNumber("maxDiscountPrice", Integer.class);

    public final NumberPath<Integer> minOrderPrice = createNumber("minOrderPrice", Integer.class);

    public final StringPath name = createString("name");

    public final BooleanPath status = createBoolean("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

