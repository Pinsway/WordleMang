module com.eesti.wordle2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.eesti.wordle2 to javafx.fxml;
    exports com.eesti.wordle2;
}