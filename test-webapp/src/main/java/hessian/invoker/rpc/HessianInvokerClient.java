package hessian.invoker.rpc;

import com.wmt.framework.test.common.User;
import com.wmt.framework.test.common.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author liyebing created on 17/2/13.
 * @version $Id$
 */
public class HessianInvokerClient {


    public static void main(String[] args) {


        ApplicationContext context = new ClassPathXmlApplicationContext("hessian-rpc-client.xml");
        UserService userService = (UserService) context.getBean("userServiceHessianProxy");
        User user = userService.findByName("kongxuan");
        System.out.println(user.getName() + "   " + user.getEmail());

    }

}
