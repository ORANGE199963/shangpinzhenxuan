package aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.common.utils.ThreadLocalUtil;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

public class AopUtil {

    public static void preTarget(SysOperLog sysOperLog, Log sysLog, ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("目标方法执行之前要做的工作");

        sysOperLog.setTitle(sysLog.title());//目标接口的标题

        //获取到当前目标方法的Method对象
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        String name = method.getDeclaringClass().getName();//类的全限定名（包名.类名）
        String methodName = method.getName();//方法名

        sysOperLog.setMethod(name +"."+ methodName+"()");//目标方法的完整的名称   包名.类名.方法名()

        //获取当前请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();//当前请求对应的一个request对象

        String requestMethod = request.getMethod();//请求的方式（类型）

        sysOperLog.setRequestMethod(requestMethod);//请求的类型  put  post  delete  get 。。。

        sysOperLog.setOperatorType(sysLog.operatorType().name());//操作人类型：MANAGE(后台用户)
        sysOperLog.setOperName(ThreadLocalUtil.getSysUser().getName());//操作人名称
        sysOperLog.setOperUrl(request.getRequestURI());// 获取当前请求的路径，不包括ip和port
        sysOperLog.setOperIp(request.getRemoteHost());//客户端的ip

        if(sysLog.isSaveRequestData()){
            //需要保存请求参数
            //目标接口的参数列表，整体转成json字符串
            String jsonString = JSON.toJSONString(proceedingJoinPoint.getArgs());
            sysOperLog.setOperParam(jsonString);
        }
    }

    public static void afterTarget(SysOperLog sysOperLog,Log sysLog,Object result,Integer status,String errorMsg){
        sysOperLog.setStatus(status);
        sysOperLog.setErrorMsg(errorMsg);
        if (sysLog.isSaveResponseData()){
            sysOperLog.setJsonResult(JSON.toJSONString(result));//保存目标方法的返回值
        }
    }

}
