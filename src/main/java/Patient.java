import java.io.Serializable;

public class Patient implements Serializable {
    private String id;
    private String latestSeverityScore;
    private String DOB;
    private String sex;
    private String lastUpdated;

    Patient(String id, String latestSeverityScore, String DOB, String sex, String lastUpdated){
        this.id = id;
        this.latestSeverityScore = latestSeverityScore;
        this.DOB = DOB;
        this.sex = sex;
        this.lastUpdated = lastUpdated;
    }
}
