module com.example.projet_exam {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires mysql.connector.j;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires jakarta.mail;
    requires java.management;

    opens com.example.projet_exam to javafx.fxml;
    exports com.example.projet_exam;
    opens entity to org.hibernate.orm.core, java.persistence;
}

