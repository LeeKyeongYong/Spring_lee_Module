package com.study.mstudy.item.valid;

import com.study.mstudy.item.constant.ItemType;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import java.lang.annotation.*;
import com.study.mstudy.item.valid.ItemTypeValid.ItemTypeValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Constraint(validatedBy = ItemTypeValidator.class)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemTypeValid {

    String message() default "허용되지 않은 물품 유형입니다.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};

    class ItemTypeValidator implements ConstraintValidator<ItemTypeValid, String> {

        @Override
        public boolean isValid(String cd, ConstraintValidatorContext context) {
            boolean hasItemType = false;
            ItemType[] itemTypeList = ItemType.values();
            for(ItemType i:itemTypeList){
                hasItemType = i.hasItemCd(cd);
                if(hasItemType)break;
            }


           return hasItemType;
        }
    }
}
