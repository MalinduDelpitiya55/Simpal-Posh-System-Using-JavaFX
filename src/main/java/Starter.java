import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starter extends Application {
    public static void main(String [] args){
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        //stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/CustomerForm.fxml"))));
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/Dashbord.fxml"))));
        stage.show();
    }
}
