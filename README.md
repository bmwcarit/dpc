## DEPENDENCIES ##

  This layer depends on:

  **poky**  
  URI: git://git.yoctoproject.org/poky.git  
  branch: dizzy  
  revision: HEAD  

  **meta-openembedded (meta-oe)**  
  URI: git://git.openembedded.org/meta-openembedded  
  subdirectory: meta-oe  
  branch: dizzy  
  revision: HEAD  

## INSTALLATION ##

  For the following instructions we assume you are on a Linux Ubuntu 14.04 system.
  However, most of the instructions should also be valid for other Linux systems.

  Install required packages for Yocto:

  ```shell
    sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm
  ```

  Download the required sources to some folder in your home directory.
  For these instructions, we use the folder ~/devel:

  ```shell
    mkdir ~/devel && cd ~/devel
    git clone -b dizzy git://git.yoctoproject.org/poky.git
    git clone -b dizzy git://git.openembedded.org/meta-openembedded
    git clone https://github.com/bmwcarit/dpc.git
  ```

  To initialize the build environment of Yocto, execute:

  ```shell
    cd ~/devel/poky
    . oe-init-build-env
  ```

  The script oe-init-build-env creates an initial build folder at ~/devel/poky/build and corresponding build configuration files at ~/devel/poky/build/conf.

  The file ~/devel/poky/build/conf/bblayers.conf defines the location of Yocto meta layers.
  Edit this file and add the absolute paths to the layers meta-oe and meta-dpc to the variable BBLAYERS.
  For example, for a user "me" the absolute paths to these two meta layers would be /home/me/devel/meta-openembedded/meto-oe and /home/me/devel/dpc/meta-dpc.

  ```shell
    BBLAYERS ?= " \
      /home/me/devel/poky/<some existing paths> \
      /home/me/devel/meta-openembedded/meta-oe \
      /home/me/devel/dpc/meta-dpc \
    "
  ```

  The file ~/devel/poky/build/conf/local.conf defines additional settings for your build.
  Edit this file to specify which distribution should be build.
  Search for the variable DISTRO and set it to our distribution "dpc":

  ```shell
    DISTRO ?= "dpc"
  ```

  Additionally, add the following line to your local.conf to choose a PREEMPT-RT Kernel (see http://git.yoctoproject.org/cgit/cgit.cgi/poky/tree/meta/recipes-rt/README):

  ```shell
    PREFERRED_PROVIDER_virtual/kernel = "linux-yocto-rt"
  ```

  The variable MACHINE of local.conf defines the target hardware for which we will cross-compile.
  Make sure it is set to

  ```shell
    MACHINE ??= "qemux86"
  ```

## USAGE ##

  To initialize the build environment, execute:

  ```shell
    cd ~/devel/poky
    . oe-init-build-env
  ```

  Your configuration setup at ~/devel/poky/build/conf will be preserved.

  In your build folder ~/devel/poky/build execute

  ```shell
    bitbake <package-name>
  ```

  to start cross compilation of the package \<package-name\>.

  You can build our *dpc* development linux image with

  ```shell
    bitbake core-image-dpc-dev
  ```

  The initial build may take some hours.

  To run the compiled linux image with the qemu virtual machine, execute:

  ```shell
    runqemu qemux86 core-image-dpc-dev nographic
  ```

  Login with the user "root" (no password).
  You can exit the qemu virtual machine by typing "ctrl+a" and then "x".

### Systemd Demo ###

  The systemd demo shows how systemd can be used to limit resource consumption for individual software components.

  First, start our *dpc* platform with

  ```shell
    runqemu qemux86 core-image-dpc-dev nographic
  ```

  We use the program *stress* (http://people.seas.harvard.edu/~apw/stress/) as our example software component.
  *stress* is a configurable workload generator.
  For example, if you want *stress* to generate a lot of workload on the CPU, start *stress* from your shell with

  ```shell
    stress -c 1 &
  ```

  Running

  ```shell
    htop
  ```

  you can observe how much CPU resources are used by *stress*.
  Exit htop by typing "q" and then kill stress with

  ```shell
    killall stress
  ```

  For demonstration how systemd can be employed to enforce limits on the resource consumption of individual software components, we execute *stress* with the same parameters as above as a systemd service:
  Start the stress-cpu-starvation service with

  ```shell
    systemctl start stress-cpu-starvation
  ```

  Again, running

  ```shell
    htop
  ```

  one can observe that this time the CPU usage of *stress* is limited to 20%.
  This corresponds to the CPUQuota configured in the corresponding systemd service configuration file at /lib/systemd/system/stress-cpu-starvation.service.

  The file /lib/systemd/system/stress-memory-starvation.service defines another systemd service demonstrating the limitation of the memory consumption of a software component.

