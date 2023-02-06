package icu.xiaobai.book.advice.account;

import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.service.AccountService;
import icu.xiaobai.book.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SignUpAdvice {
    private final AccountService accountService;

    public SignUpAdvice(AccountService accountService) {
        this.accountService = accountService;
    }

    @Pointcut("execution(* icu.xiaobai.book.service.AccountService.addUser(..))")
    public void pointcut() {
    }

    /**
     * 用户添加函数参数校验函数
     * 校验 邮箱、密码是否符合要求。
     *
     * @param joinPoint joinPoint
     * @return 如果不符合参数规定的要求，返回400. 否则执行AddUser()函数
     * @throws Throwable 运行错误
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // AddUser(User user), 只有一个参数，直接强转
        Object[] args = joinPoint.getArgs();
        User user = (User) args[0];
        log.debug(user + "\t 开始注册用户");

        if (!RegexUtil.checkEmail(user.getEmail())) {
            log.debug(user + "\t email 校验失败");
            return new CustomResponse<>(HttpStatus.BAD_REQUEST);
        }

        if (accountService.checkEmailUsed(user.getEmail())) {
            log.debug(user + "\t email 已经被注册");
            return new CustomResponse<>(HttpStatus.BAD_REQUEST);
        }

        if (!RegexUtil.checkPassword(user.getPassword())) {
            log.debug(user + "\t password不符合要求");
            return new CustomResponse<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getAge() == 0) {
            log.debug(user + "\t age 不符合要求");
            return new CustomResponse<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getSex().length() == 0) {
            log.debug(user + "\t sex 不符合要求");
            return new CustomResponse<>(HttpStatus.BAD_REQUEST);
        }

        return joinPoint.proceed();
    }
}
