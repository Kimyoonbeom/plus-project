package com.example.plusproject.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCoupon is a Querydsl query type for UserCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCoupon extends EntityPathBase<UserCoupon> {

    private static final long serialVersionUID = -1859945250L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCoupon userCoupon = new QUserCoupon("userCoupon");

    public final QCoupon coupon;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> issuedAt = createDateTime("issuedAt", java.time.LocalDateTime.class);

    public final com.example.plusproject.domain.order.entity.QOrder order;

    public final BooleanPath used = createBoolean("used");

    public final DateTimePath<java.time.LocalDateTime> usedAt = createDateTime("usedAt", java.time.LocalDateTime.class);

    public final com.example.plusproject.domain.user.entity.QUser user;

    public QUserCoupon(String variable) {
        this(UserCoupon.class, forVariable(variable), INITS);
    }

    public QUserCoupon(Path<? extends UserCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCoupon(PathMetadata metadata, PathInits inits) {
        this(UserCoupon.class, metadata, inits);
    }

    public QUserCoupon(Class<? extends UserCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
        this.order = inits.isInitialized("order") ? new com.example.plusproject.domain.order.entity.QOrder(forProperty("order"), inits.get("order")) : null;
        this.user = inits.isInitialized("user") ? new com.example.plusproject.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

