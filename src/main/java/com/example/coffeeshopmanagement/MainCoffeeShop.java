package com.example.coffeeshopmanagement;

import com.example.coffeeshopmanagement.controller.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainCoffeeShop extends Application {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void start(Stage stage) throws IOException {
        StageManager stageManager = new StageManager();
        stageManager.setCurrentStage(stage);
        stageManager.loadLoginStage();

        // Lên lịch thực thi tác vụ sau mỗi khoảng thời gian cụ thể
//        scheduler.scheduleAtFixedRate(this::scheduledTask, 0, 1, TimeUnit.DAYS);
    }

    @Override
    public void stop() {
        // Đảm bảo dừng scheduler khi ứng dụng kết thúc
        scheduler.shutdown();
    }

//    public void scheduledTask() {
//        // Thực hiện các tác vụ cần lên lịch ở đây
//        new MembersRepository().updateMembershipStatusBasedOnEndDate();
//    }

    public static void main(String[] args) {
        launch();
    }
}