# meta-morse: A Yocto layer for Morse Micro software

This repo integrates Morse Micro software (Morse release v1.12.4) into the Yocto build system for embedded Linux (Yocto release Kirkstone). It has been tested to work with the Morse Micro EKH01-03 Evaluation Kit, which contains a Raspberry Pi 4B. Adaptations can be made to get it to work with other target architectures.

## Environment set-up
Here are the steps to get a vanilla image working for the Raspberry Pi 4B. You can skip this step if you already have it - just mind that I've tested this only on Kirkstone release of Yocto.

#### Host Dependencies
```
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file locales libacl1
```
#### Vanilla RPi 4B build
```
cd ~
mkdir yocto
cd yocto
mkdir sources

# Install required layers with kirkstone release
cd sources
git clone git://git.yoctoproject.org/poky -b kirkstone
git clone git://git.yoctoproject.org/meta-raspberrypi -b kirkstone
git clone https://git.openembedded.org/meta-openembedded -b kirkstone

# Source build environment
cd ~/yocto
source sources/poky/oe-init-build-env
```
This last line creates the ```yocto/build``` folder. In ```build/conf/local.conf``` set the machine. I also added the to limit the number of cores Yocto uses to 4, which avoids OOM issues.

```
      ## build/conf/local.conf
#Setup RPi build
MACHINE = "raspberrypi4"
BB_NUMBER_THREADS ?= "4"
PARALLEL_MAKE ?= "-j 4"
```

My ```build/conf/bblayers.conf``` is:
```
# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  ${TOPDIR}/../sources/poky/meta \
  ${TOPDIR}/../sources/poky/meta-poky \
  ${TOPDIR}/../sources/poky/meta-yocto-bsp \
  ${TOPDIR}/../sources/meta-raspberrypi \
  ${TOPDIR}/../sources/meta-openembedded/meta-oe \
  ${TOPDIR}/../sources/meta-openembedded/meta-multimedia \
  ${TOPDIR}/../sources/meta-openembedded/meta-networking \
  ${TOPDIR}/../sources/meta-openembedded/meta-python \
  ${TOPDIR}/../sources/meta-morse \
  "
```
You can now create the image with ```bitbake core-image-base```. This may take several hours. Note that there are some required wireless networking dependencies that are built in ```core-image-base``` that aren't in ```core-image-minimal```. I have not yet picked apart exactly what they are, so I stuck with ```core-image-base```.

Once it's built, flash your MicroSD card with 
```
cd ~/yocto/build
bzcat tmp/deploy/images/raspberrypi4/core-image-base-raspberrypi4.wic.bz2 | sudo dd of=/dev/sdX bs=1M && sync
```
Changing ```sdX``` to whatever is appropriate for your SD card. You can now verify that linux boots on the RPi.

## Adding the meta-morse layer
```
cd ~/yocto/sources
git clone https://github.com/LOflynn/meta-morse.git
```
This layer includes recipes for The Morse Driver (morse-driver), hostapd_s1g (morse-hostap), command line (morse-cli), and for deploying firmware binaries (morse-bin). It also includes EKH01-03 specific recipes, inside morse-rpi4. These recipes patch the linux kernel, add to the device tree, and change kernel configuration.

#### Morse patches

The patches might need some attention. The Morse kernel patches for version 5.15.x must be adapted to work for the specific sub-sub-version your kernel is. To find the version, you can run:
```
bitbake virtual/kernel -e | grep KERNEL_VERSION=
```

Mine was ```5.15.92-v7l```. With a bit of luck if yours matches then the patches in the repo will work just fine, otherwise heres the steps to get it adpated.

Ensure you have the morse pathches for release ```release v1.12.4``` downloaded elsewhere.

```
# Navigate to kernel source 
cd build/tmp/work-shared/raspberrypi4/kernel-source/
cat ~/morsemicro_kernel_patches_rel_1_12_4_2024_Jun_11/5.15.x/*.patch | patch -g0 -p1 -E -d .
```

Any failed patches will generate ```.rej``` files. Inspect those and apply the changes manually as required. For me, ```include/linux/mmc/card.h``` and ```net/mac80211/agg-rx.c``` needed attention.

When you're happy, create new patch that includes all these changes, and add it to the correct place in ```morse-rpi```

```
# (Still inside build/tmp/work-shared/raspberrypi4/kernel-source/)
# Add patch to recipe
git diff -p > ~/yocto/sources/meta-morse/recipes-morse/morse-rpi4/files/morse_patches.patch 

# Reset changes 
git reset --hard
```
#### Add the layer and rebuild
Now you can add the morse layers and packages to your build. 
Add the ```meta-morse``` layer to ```bblayers.conf```, and in ```local.conf``` add:
```
...
#Setup RPi build
MACHINE = "raspberrypi4"
BB_NUMBER_THREADS ?= "4"
PARALLEL_MAKE ?= "-j 4"

# Add packages and device tree overlays to image
KERNEL_DEVICETREE:append = " overlays/mm_wlan-overlay.dtbo overlays/morse-ps-overlay.dtbo"
IMAGE_INSTALL:append = " morse-cli morse-hostap morse-driver morse-bin"
...
```

You can now rebuild the image with ```bitbake core-image-base```. The kernel will detect the Morse hatinsert the driver automatically on boot.
