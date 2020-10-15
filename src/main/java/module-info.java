module org.una.unaeropuertoclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.ws.rs;
    requires java.base;
    requires com.jfoenix;
    requires gson;
    requires java.net.http;
    requires java.sql;
    requires javafx.graphics;


    opens org.una.unaeropuertoclient to javafx.fxml;
    exports org.una.unaeropuertoclient;
    exports org.una.unaeropuertoclient.controller;
}
