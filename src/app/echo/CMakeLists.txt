cmake_minimum_required(VERSION 2.8.8)

find_package(Java REQUIRED)

include(UseJava)

project (dsn.java.app.echo)

file("GLOB_RECURSE"
	TEMP_PROJ_JAVA_SRC
	"${CMAKE_CURRENT_SOURCE_DIR}/*.java"
	)
	
add_jar(dsn_dev_java_jar ${TEMP_PROJ_JAVA_SRC} VERSION 1.0.0)

get_target_property(_jarFile dsn.java.app.echo JAR_FILE)
get_target_property(_classDir dsn.java.app.echo CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")
