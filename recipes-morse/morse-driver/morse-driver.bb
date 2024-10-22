DESCRIPTION = "Build and install the morse driver"

TAG_NAME = "1.12.4"
PV = "${TAG_NAME}+git${SRCPV}"
SRC_URI = "git://github.com/MorseMicro/morse_driver.git;protocol=https;branch=main;tag=${TAG_NAME}"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit module

S = "${WORKDIR}/git"
B = "${S}"

do_compile() {
	bbnote $(pwd)
	oe_runmake CONFIG_WLAN_VENDOR_MORSE=m CONFIG_MORSE_SDIO=y CONFIG_MORSE_USER_ACCESS=y CONFIG_MORSE_VENDOR_COMMAND=y
}

do_install() {
	install -d ${D}/lib/modules/${KERNEL_VERSION}/
	
	install -m 0644 morse.ko ${D}/lib/modules/${KERNEL_VERSION}/morse.ko
	install -m 0644 dot11ah/dot11ah.ko ${D}/lib/modules/${KERNEL_VERSION}/dot11ah.ko
}

