// open does open app fore reflection
open module app {
  requires javafx.controls;
  requires javafx.fxml;

  requires java.xml.bind;
  requires com.sun.xml.bind;

  requires ormlite.jdbc;
  requires ormlite.core;
  requires java.persistence;
  requires org.postgresql.jdbc;
  requires java.sql;
  requires org.jsoup;
  requires json.path;
  requires org.slf4j;

  exports app;
  exports app.gui;
  exports app.model;
}