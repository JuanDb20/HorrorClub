package Model;

public class Evaluation {

    private double score;
    private String comment;
    private boolean accepted;

    public Evaluation(boolean accepted, String comment, double score) {
        this.accepted = accepted;
        this.comment = comment;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isValidInfoEvaluation() {
        if (score < 0 || score > 10) {  //El score debe estar entre 0 y 10
            return false;
        }

        if (comment == null || comment.trim().isEmpty()) {  //El comentario no puede estar vacio
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "score=" + score +
                ", comment='" + comment + '\'' +
                ", accepted=" + accepted +
                '}';
    }
}
