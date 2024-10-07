SUMMARY = "morse driver"
DESCRIPTION = "morse driver description"
HOMEPAGE = "https://myapp.example.com"

SRC_URI = "git:///home/Lewis.OFlynn/src/morse_driver/;protocol=file;branch=main"
SRCREV = "df39563b9c192af16901e5912d9e4ab6ef4e11e1"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit module

S = "${WORKDIR}/git"
B = "${S}"

do_compile() {
	bbnote $(pwd)
	oe_runmake CONFIG_WLAN_VENDOR_MORSE=m CONFIG_MORSE_SDIO=y CONFIG_MORSE_USER_ACCESS=y CONFIG_MORSE_VENDOR_COMMAND=y CONFIG_MORSE_ENABLE_TEST_MODES=y
}

do_install() {
	install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/
	install -d ${D}/lib/modules/${KERNEL_VERSION}/extra/dot11ah/
	
	install -m 0644 morse.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/morse.ko
	install -m 0644 dot11ah/dot11ah.ko ${D}/lib/modules/${KERNEL_VERSION}/extra/dot11ah/dot11ah.ko
}
RDEPENDS_${PN} += "kernel-module-morse-5.15.71+5ebe23fd15"
