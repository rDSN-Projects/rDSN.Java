set(MY_PROJ_NAME dsn.dev.java_helper)

# Source files under CURRENT project directory will be automatically included.
# You can manually set MY_PROJ_SRC to include source files under other directories.
set(MY_PROJ_SRC "")

# Search mode for source files under CURRENT project directory?
# "GLOB_RECURSE" for recursive search
# "GLOB" for non-recursive search
set(MY_SRC_SEARCH_MODE "GLOB_RECURSE")

set(MY_PROJ_INC_PATH "C:\\Program Files\\Java\\jdk1.8.0_51\\include" "C:\\Program Files\\Java\\jdk1.8.0_51\\include\\win32")

set(MY_PROJ_LIBS "jawt" "jvm")

set(MY_PROJ_LIB_PATH "C:\\Program Files\\Java\\jdk1.8.0_51\\lib")

# Extra files that will be installed
set(MY_BINPLACES "")

dsn_add_shared_library()