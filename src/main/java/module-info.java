module me.goraj.monkeytypejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.goraj.monkeytypejavafx to javafx.fxml;
    exports me.goraj.monkeytypejavafx;
}