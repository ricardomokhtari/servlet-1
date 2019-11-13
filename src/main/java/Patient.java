import java.io.Serializable;

public class Patient implements Serializable {
    private String name;
    private int phoneNum;
    private int id;

    public Patient(String name, int phoneNum, int id){
        this.name = name;
        this.phoneNum = phoneNum;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public int getPhoneNum(){
        return phoneNum;
    }

    public int getId(){
        return id;
    }
}
