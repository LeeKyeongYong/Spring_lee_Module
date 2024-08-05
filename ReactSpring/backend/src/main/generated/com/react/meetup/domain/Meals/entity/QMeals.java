package com.react.meetup.domain.Meals.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMeals is a Querydsl query type for Meals
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeals extends EntityPathBase<Meals> {

    private static final long serialVersionUID = -1546729827L;

    public static final QMeals meals = new QMeals("meals");

    public final StringPath creator = createString("creator");

    public final StringPath creatorEmail = createString("creatorEmail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath instructions = createString("instructions");

    public final StringPath slug = createString("slug");

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    public QMeals(String variable) {
        super(Meals.class, forVariable(variable));
    }

    public QMeals(Path<? extends Meals> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMeals(PathMetadata metadata) {
        super(Meals.class, metadata);
    }

}

