module com.example.maybe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.junit.jupiter.api;
    requires junit;
    requires java.prefs;
    requires log4j.api;
    requires jbcrypt;
    requires PDFGenerator;


    opens zolci;
    opens zolci.Controllers;
    opens zolci.DAO;
    opens zolci.models;
    opens zolci.Controllers.Admin;
    opens zolci.Controllers.User;
    opens zolci.Controllers.Manager;
}