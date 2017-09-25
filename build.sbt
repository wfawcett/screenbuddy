name := """screenbuddy"""
organization := "com.screenbuddy"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies ++= Seq(
  ws
)
libraryDependencies += ehcache