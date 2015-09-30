set(MY_PROJ_NAME "dsn.dev.java_helper")
project(${MY_PROJ_NAME} C CXX)

# Source files under CURRENT project directory will be automatically included.
# You can manually set MY_PROJ_SRC to include source files under other directories.
set(MY_PROJ_SRC "")

# Search mode for source files under CURRENT project directory?
# "GLOB_RECURSE" for recursive search
# "GLOB" for non-recursive search
set(MY_SRC_SEARCH_MODE "GLOB_RECURSE")

if(WIN32)
	set(MY_PROJ_INC_PATH "${JAVA_HOME}/include" "${JAVA_HOME}/include/win32")
else()
	set(MY_PROJ_INC_PATH "${JAVA_HOME}/include")
endif()

set(MY_PROJ_LIBS "jawt" "jvm")

set(MY_PROJ_LIB_PATH "${JAVA_HOME}/lib")

# Extra files that will be installed
set(MY_BINPLACES "")

set(MY_BOOST_PACKAGES "")

dsn_common_setup()
dsn_add_shared_library()
