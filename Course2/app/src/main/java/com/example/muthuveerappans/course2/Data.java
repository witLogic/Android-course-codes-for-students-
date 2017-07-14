package com.example.muthuveerappans.course2;

/**
 * Created by muthuveerappans on 7/14/17.
 */

public class Data {
    public static final String[][] LOGIN_DATA = {
            {"swaminathan", "wildcards"},
            {"vinoth", "1234554321"},
            {"abdul", "kalamlegend"},
            {"steve", "jobsbusiness"}
    };

    public static final boolean checkLoginData(String userName, String password) {
        for (String[] loginData : LOGIN_DATA) {
            String validUserName = loginData[0];
            String validPassword = loginData[1];

            if (validPassword.equals(password) && validUserName.equals(userName)) {
                return true;
            }
        }

        return false;
    }
}
