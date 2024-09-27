package com.jqstudy.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = -2076691480L;

    public static final QEmployee employee = new QEmployee("employee");

    public final NumberPath<Double> commissionPct = createNumber("commissionPct", Double.class);

    public final NumberPath<Integer> departmentId = createNumber("departmentId", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> employeeId = createNumber("employeeId", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final DatePath<java.time.LocalDate> hireDate = createDate("hireDate", java.time.LocalDate.class);

    public final StringPath jobId = createString("jobId");

    public final StringPath lastName = createString("lastName");

    public final NumberPath<Integer> managerId = createNumber("managerId", Integer.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Double> salary = createNumber("salary", Double.class);

    public QEmployee(String variable) {
        super(Employee.class, forVariable(variable));
    }

    public QEmployee(Path<? extends Employee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmployee(PathMetadata metadata) {
        super(Employee.class, metadata);
    }

}

