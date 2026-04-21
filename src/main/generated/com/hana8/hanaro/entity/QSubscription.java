package com.hana8.hanaro.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubscription is a Querydsl query type for Subscription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscription extends EntityPathBase<Subscription> {

    private static final long serialVersionUID = -1276438446L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubscription subscription = new QSubscription("subscription");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QAccount account;

    public final BooleanPath canceled = createBoolean("canceled");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> interestRate = createNumber("interestRate", Double.class);

    public final QMember member;

    public final NumberPath<Integer> paidCount = createNumber("paidCount", Integer.class);

    public final NumberPath<Long> paymentAmount = createNumber("paymentAmount", Long.class);

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final QProduct product;

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<com.hana8.hanaro.common.enums.SubscriptionStatus> status = createEnum("status", com.hana8.hanaro.common.enums.SubscriptionStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSubscription(String variable) {
        this(Subscription.class, forVariable(variable), INITS);
    }

    public QSubscription(Path<? extends Subscription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubscription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubscription(PathMetadata metadata, PathInits inits) {
        this(Subscription.class, metadata, inits);
    }

    public QSubscription(Class<? extends Subscription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account"), inits.get("account")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

