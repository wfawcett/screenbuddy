name := """screenbuddy"""
organization := "com.screenbuddy"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"

libraryDependencies ++= Seq(
  ws,
  ehcache,
  jdbc
)