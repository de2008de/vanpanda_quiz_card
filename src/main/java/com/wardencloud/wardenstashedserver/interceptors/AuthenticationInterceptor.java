package com.wardencloud.wardenstashedserver.interceptors;

import com.wardencloud.wardenstashedserver.entities.User;
import com.wardencloud.wardenstashedserver.jwt.annotations.PassToken;
import com.wardencloud.wardenstashedserver.services.TokenService;
import com.wardencloud.wardenstashedserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Value("${token.secret}")
    private String tokenSecret;

    private final String USER_DOES_NOT_EXIST_MESSAGE = "User does not exist";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            return true;
        }
        String token = request.getHeader("token");
        if (token == null) {
            // TODO: When we have an error, Spring will send a new error request without token. Therefore, throwing token empty exception here will confuse people. Should find a proper solution.
            return false;
        }
        int userId = tokenService.getUserIdFromToken(token);
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException(USER_DOES_NOT_EXIST_MESSAGE);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}