SUMMARY = "hostapd_s1g"

TAG_NAME = "1.12.4"
PV = "${TAG_NAME}+git${SRCPV}"
SRC_URI = "git://github.com/MorseMicro/hostap.git;protocol=https;branch=v1.12;tag=${TAG_NAME}"


LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ebcb90236d1ad640558c3d3cd3035df"


inherit autotools
DEPENDS += "libnl openssl"
RDEPENDS_${PN} += "libnl openssl"

CFLAGS:append = " -I${STAGING_INCDIR}/libnl3/ -I${STAGING_INCDIR}/"
LDFLAGS:append = " -L${STAGING_LIBDIR}/"
LIBS:append = " -lnl-3 -lm -lpthread -lcrypto -lssl"

S = "${WORKDIR}/git"
B = "${S}/hostapd"

do_configure() {
    cp ./defconfig ./.config
}

do_compile() {
    oe_runmake MORSE_VERSION=rel_1_12_4_2024_Jun_11 -C .
}

do_install() {  
    export BINDIR="/usr/sbin"
    export DESTDIR="${D}"
    oe_runmake install
}

