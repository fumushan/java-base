package com.base.util;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: lukew
 * Date: 2017-09-29
 * Time: 11:47
 */
public class PermissionUtil {

    public static String getPermission(String url){

       String permission = url.replaceAll("/", ".").replaceAll("\\{","").replaceAll("}","");;
        if(permission.startsWith(".")){
            permission = permission.replaceFirst(".","");
        }
        return permission;
    }
}
