require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "The DPC image for use during development, which includes \
  certain development tools for debugging and inspection"

LICENSE = "MIT"

IMAGE_INSTALL += "\
  packagegroup-core-tools-debug \
  packagegroup-core-ssh-openssh \
  htop \
  dpc-systemd-demo \
"

IMAGE_FEATURES += " package-management dbg-pkgs "
