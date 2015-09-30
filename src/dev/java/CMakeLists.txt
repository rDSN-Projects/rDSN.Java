cmake_minimum_required(VERSION 2.8.8)

find_package(Java REQUIRED)

include(UseJava)

project (dsn.dev.java)

#set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.6" "-target" "1.6")

file("GLOB_RECURSE"
	TEMP_PROJ_JAVA_SRC
	"${CMAKE_CURRENT_SOURCE_DIR}/*.java"
	)
	
add_jar(dsn_dev_java_jar ${TEMP_PROJ_JAVA_SRC} VERSION 1.0.0)

get_target_property(_jarFile dsn.dev.java JAR_FILE)
get_target_property(_classDir dsn.dev.java CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")
