SUMMARY = "morse bin"
DESCRIPTION = "deploy morse binaries"
HOMEPAGE = "https://myapp.example.com"
LICENSE="CLOSED"

SRC_URI = "file://bcf_mf08551.bin \
		file://mm6108.bin"
		
S = "${WORKDIR}"

do_install() {
	install -d ${D}/lib/firmware/morse/
	install -m 0644 bcf_mf08551.bin ${D}/lib/firmware/morse/bcf_default.bin
	install -m 0644 mm6108.bin ${D}/lib/firmware/morse/mm6108.bin
}

FILES:${PN} += "/lib/firmware/morse/*"
INSANE_SKIP:${PN} = "arch"
