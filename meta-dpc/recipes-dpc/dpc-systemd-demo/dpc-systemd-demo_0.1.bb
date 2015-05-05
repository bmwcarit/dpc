SUMMARY = "Simple systemd demo configuration files"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${THISDIR}/../../../LICENSE;md5=2bd38b041a10605f418d67d3bfd54de4"

DEPENDS = "systemd"
RDEPENDS_${PN} = "stress"

SRC_URI += " \
  file://CMakeLists.txt \
  file://services/stress-cpu-starvation.service \
  file://services/stress-memory-starvation.service \
"

S = "${WORKDIR}"

inherit cmake systemd

SYSTEMD_SERVICE_${PN} = "stress-cpu-starvation.service stress-memory-starvation.service"

# do not run the demo services automatically at startup
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
