import java.io.Serializable;

public class Record implements Serializable{
    private String id;
    private String date;
    private String imageage;
    private String erythemascore;
    private String edemascore;
    private String exclorationscore;
    private String lichenificationscore;
    private String areascore;
    private float totalscore;
    private String comments;

    Record(String id, String date, String imageage, String erythemascore, String edemascore, String exclorationscore, String lichenificationscore, String areascore, float totalscore, String comments){
        this.id = id;
        this.date = date;
        this.imageage = imageage;
        this.erythemascore = erythemascore;
        this.edemascore = edemascore;
        this.exclorationscore = exclorationscore;
        this.lichenificationscore = lichenificationscore;
        this.areascore = areascore;
        this.totalscore = totalscore;
        this.comments = comments;
    }

}
