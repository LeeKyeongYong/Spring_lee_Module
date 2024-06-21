package com.example.module_app_member.global.jpa.entity;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEntity implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Override
    public boolean isNew() {
        return id == null;
    }

    public String getModelName() {
        String simpleName = this.getClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}