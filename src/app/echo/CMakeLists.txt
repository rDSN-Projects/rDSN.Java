set(MY_PROJ_NAME "dsn.java.app.echo")

# Source files under CURRENT project directory will be automatically included.
# You can manually set MY_PROJ_SRC to include source files under other directories.
set(MY_PROJ_SRC "")

# Search mode for source files under CURRENT project directory?
# "GLOB_RECURSE" for recursive search
# "GLOB" for non-recursive search
set(MY_SRC_SEARCH_MODE "GLOB")

set(MY_PROJ_INC_PATH "")

set(MY_PROJ_LIBS "dsn.dev.java")

set(MY_PROJ_LIB_PATH "")

# Extra files that will be installed
set(MY_BINPLACES "config.ini")

set(MY_BOOST_PACKAGES "")

dsn_add_java_executable()
