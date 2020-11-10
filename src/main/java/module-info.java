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
    requires jasperreports;
    //opens org.una.unaeropuertoclient to javafx.fxml;
    opens org.una.unaeropuertoclient.controller to javafx.fxml, javafx.controls, com.jfoenix;
    exports org.una.unaeropuertoclient to javafx.graphics;
    exports org.una.unaeropuertoclient.model;
    //  exports org.una.unaeropuertoclient;
    exports org.una.unaeropuertoclient.controller;

//    requires javafx.sw
}
