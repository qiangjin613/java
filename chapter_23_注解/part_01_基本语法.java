import atunit.Test;
import atunit.UseCase;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Testable {
    public void execute() {
        System.out.println("executing...");
    }

    @Test
    void testExecute() {
        execute();
    }
}


/**
 * 使用自定义的 @UseCase 注解
 */
class PasswordUtils {
    @UseCase(id = 47, description = "Passwords must contain at least one numeric")
    public boolean validatePassword(String passwd) {
        return (passwd.matches("\\w*\\d\\w*"));
    }

    @UseCase(id = 48)
    public String encryptPassword(String passwd) {
        return new StringBuilder(passwd).reverse().toString();
    }

    @UseCase(id = 49, description = "New passwords can't equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPasswords, String passwd) {
        return !prevPasswords.contains(passwd);
    }
}


/**
 * 【编写注解处理器】
 * 使用注解中一个很重要的部分就是，创建与使用注解处理器。
 *
 * 下面编写一个非常简单的注解处理器，用它来读取被注解的 PasswordUtils 类，
 * 并且使用反射机制来寻找 @UseCase 标记。
 */
class UseCaseTracker {
    public static void trackUseCases(List<Integer> useCases, Class<?> c1) {
        for (Method m : c1.getDeclaredMethods()) {
            UseCase uc = m.getAnnotation(UseCase.class);
            if (uc != null) {
                System.out.println("Found Use Case " + uc.id() + "\n" + uc.description());
                useCases.remove(Integer.valueOf(uc.id()));
            }
        }
        useCases.forEach(i -> System.out.println("Missing use case " + i));
    }

    public static void main(String[] args) {
        List<Integer> useCases = IntStream.range(47, 51).boxed().collect(Collectors.toList());
        trackUseCases(useCases, PasswordUtils.class);
    }
}

















