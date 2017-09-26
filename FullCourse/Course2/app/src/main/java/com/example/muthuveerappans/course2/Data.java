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

    public static final String[][] PUZZLE_DATA = {
            {"1", "A safe has a code lock that unlocks if you input the correct four digits, in any order. The lock has a keypad the the digits 0, 1, 2, ..., 9. For example, suppose the unlock code is 1000. The safe will open for any order you input the digits: 1000, 0100, 0010, 0001. How many different unlock codes are there? (Two unlock codes are different if they do not contain exactly the same digits.)", "715"},
            {"2", "There are 25 mechanical horses and a single racetrack. Each horse completes the track in a pre-programmed time, and the horses all have different finishing times, unknown to you. You can race 5 horses at a time. After a race is over, you get a printout with the order the horses finished, but not the finishing times of the horses. What is the minimum number of races you need to identify the fastest 3 horses?", "7"}
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

    public static final String[] getPuzzleData(int questionID) {
        for (String[] puzzleData : PUZZLE_DATA) {
            int id = Integer.valueOf(puzzleData[0]);

            if (id == questionID) {
                return puzzleData;
            }
        }

        return null;
    }

    public static final int getPuzzleCount() {
        return PUZZLE_DATA.length;
    }
}
