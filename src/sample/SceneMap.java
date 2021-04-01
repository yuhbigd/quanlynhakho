package sample;


import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;

import java.io.IOException;
import java.util.*;
/*
    Su dung singleton design pattern va dua tren stage machine
*/

public class SceneMap {

    private Stack<String> next;


    private Stage stage;

    //stack chua cac scene

    private Map<String,Scene> scene;

    //hashMap chua FXMLloader

    String recentPath;

    private Map<String,FXMLLoader> getControl;

    // khoi tao singleton

    private static SceneMap instance=null;
    private SceneMap() {
        next = new Stack<String>();
        scene = new HashMap<>();
        getControl=new HashMap<>();
    }
    public static SceneMap getInstances() {
        if(instance==null) {
            instance=new SceneMap();
        }
        return instance;
    }
    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void setScene(String path) throws Exception {
        recentPath=path;
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        getControl.put(path,loader);
        Parent root=getControl.get(path).load();
        Scene s= new Scene(root);
        scene.put(path,s);
        stage.setScene(s);
    }
    public void changeScene(String path) throws Exception {
        popScene();
        recentPath=path;
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        getControl.put(path,loader);
        Parent root=getControl.get(path).load();
        Scene s= new Scene(root);
        scene.put(path,s);
        stage.setScene(s);
    }
    public void set_Scene_Map_Had(String path) {
        if(scene.containsKey(path)) {
            recentPath=path;
            stage.setScene(scene.get(path));
        }
    }
    public void popScene() {
        if(!scene.isEmpty()) {
            scene.remove(recentPath);
        }
    }
    public void popLoader(String path)throws IOException {
        if(!getControl.isEmpty()) {
            getControl.remove(path);
        }
    }
    public Scene getScene(String path)
    {
        return scene.get(path);
    }
    public FXMLLoader getLoader(String path) throws IOException {

            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            getControl.put(path,loader);
            Parent root=getControl.get(path).load();
            Scene s= new Scene(root);
            return loader;
    }

    public void setCssforScene(String pathtoFXML,String pathToCss) {
        scene.get(pathtoFXML).getStylesheets().add(getClass().getResource(pathToCss).toExternalForm());
    }


    public FXMLLoader getnode(String path) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            return loader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addNextPath(String a){
        next.push(a);
    }
    public Stack<String> getN(){
        return next;
    }
    public String getNext() {
        return next.peek();
    }
    public  String popNextP(){
        return next.pop();
    }
}
