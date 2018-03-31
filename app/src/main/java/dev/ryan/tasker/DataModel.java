package dev.ryan.tasker;

/**
 * Created by ryan on 05/03/18.
 */

public class DataModel {
    int id;
    String task;
    String time;
    String description;

    public DataModel(int id, String task, String time, String description) {
        this.id=id;
        this.task=task;
        this.time=time;
        this.description=description;

    }

    public int getId(){
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

}