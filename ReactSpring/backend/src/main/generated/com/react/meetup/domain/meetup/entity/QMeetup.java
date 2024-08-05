package com.react.meetup.domain.meetup.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMeetup is a Querydsl query type for Meetup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeetup extends EntityPathBase<Meetup> {

    private static final long serialVersionUID = 754982555L;

    public static final QMeetup meetup = new QMeetup("meetup");

    public final StringPath address = createString("address");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath title = createString("title");

    public QMeetup(String variable) {
        super(Meetup.class, forVariable(variable));
    }

    public QMeetup(Path<? extends Meetup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMeetup(PathMetadata metadata) {
        super(Meetup.class, metadata);
    }

}

