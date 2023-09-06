package com.example.coffeeshopmanagement.controller;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import javafx.stage.Stage;

public class ShowProductController {
    private JDBCConnect jdbcConnect;

    private Stage stage;
    private StageManager stageManager = new StageManager();

    public ShowProductController(){
        this.jdbcConnect = new JDBCConnect();
        this.stageManager = new StageManager();
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
