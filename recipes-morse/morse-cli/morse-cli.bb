SUMMARY = "morse_cli"
DESCRIPTION = "morse_cli description"
HOMEPAGE = "https://myapp.example.com"

SRC_URI = "git:///home/Lewis.OFlynn/src/morse_cli/;protocol=file;branch=main"
SRCREV = "5dd8644208091b5fad14516649409e7efe63a204"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSING;md5=fb9bb67f7333939b51bb986dc3a6cdd1"


inherit autotools
DEPENDS += "libnl"
RDEPENDS_${PN} += "libnl"

CFLAGS:append = " -I${STAGING_INCDIR}/libnl3/"
LDFLAGS:append = " -L${STAGING_LIBDIR}/ -lnl-3 -lnl-genl-3"

S = "${WORKDIR}/git"
B = "${S}"

do_compile() {
    oe_runmake CONFIG_MORSE_TRANS_NL80211=1
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 morse_cli ${D}${bindir}/morse_cli           # -, not _
}

