module com.example.sprint1ms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.graphics;
    requires java.compiler;
    requires jdk.httpserver;
    requires java.logging;
    requires java.management;
    requires org.apache.logging.log4j;


    opens com.example.sprint1ms to javafx.fxml;
    exports com.example.sprint1ms;
}