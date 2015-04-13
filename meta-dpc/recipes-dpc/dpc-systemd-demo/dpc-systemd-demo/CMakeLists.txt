cmake_minimum_required(VERSION 2.8.1)
project(dpc-systemd-demo)

find_package(PkgConfig REQUIRED)
pkg_check_modules(SYSTEMD REQUIRED "systemd")
execute_process(COMMAND ${PKG_CONFIG_EXECUTABLE} systemd --variable=systemdsystemunitdir
	OUTPUT_STRIP_TRAILING_WHITESPACE
	OUTPUT_VARIABLE _systemd_service_install_dir
)

set(_systemd_service_files
	services/stress-cpu-starvation.service
	services/stress-memory-starvation.service
)

install(FILES ${_systemd_service_files} DESTINATION ${_systemd_service_install_dir})
