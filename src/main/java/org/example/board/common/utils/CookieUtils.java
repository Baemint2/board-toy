//package org.example.board.common.utils;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class CookieUtils {
//
//    public static void create(HttpServletResponse response, String name, String value, Boolean secure, int maxAge, String path) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(secure);
//        cookie.setMaxAge(maxAge);
//        cookie.setPath(path);
//        response.addCookie(cookie);
//    }
//
//    public static String getCookieValue(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null) {
//            for (Cookie cookie : cookies) {
//                if(name.equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
