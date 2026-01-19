package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateSpendingExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({UserExtension.class, CategoryExtension.class, CreateSpendingExtension.class})
public @interface User {

    String username() default "";

    SpendingCategory[] categories() default {};

    Spending[] spendings() default {};

    int incomeInvitationsCount() default 0;

    int outcomeInvitationsCount() default 0;

    int friendsCount() default 0;


}
