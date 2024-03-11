package aspect;

import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Autowired
    SysOperLogService sysOperLogService;




    @Around(value = "@annotation(sysLog)")
    public Object aop(ProceedingJoinPoint proceedingJoinPoint , Log sysLog){
        Object result = null;
        SysOperLog sysOperLog = new SysOperLog();
        try {


            AopUtil.preTarget(sysOperLog,sysLog,proceedingJoinPoint);

            result = proceedingJoinPoint.proceed();//去调用目标方法，获取到返回值

            AopUtil.afterTarget(sysOperLog,sysLog,result,0,null);

        } catch (Throwable e) {

            AopUtil.afterTarget(sysOperLog,sysLog,result,1,e.getMessage());

            throw new RuntimeException(e);
        }finally {
            //将sysOperLog添加到数据库中
            sysOperLogService.addSysOperLog(sysOperLog);
        }

        return result;
    }

}
