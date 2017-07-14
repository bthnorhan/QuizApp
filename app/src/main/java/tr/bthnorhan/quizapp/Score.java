package tr.bthnorhan.quizapp;

/**
 * Created by bthnorhan on 14.07.2017.
 */

public class Score {
    private String user;
    private int score;

    public Score() {}

    public Score(String user, int score) {
        this.user = user;
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
