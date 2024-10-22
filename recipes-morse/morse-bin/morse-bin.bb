SUMMARY = "Morse Binaries"
DESCRIPTION = "Deploy the firmware and Board Configuration File binaries"
LICENSE="CLOSED"

SRC_URI = "https://github.com/MorseMicro/firmware_binaries/releases/download/v1.12.4/morsemicro-fw-rel_1_12_4_2024_Jun_11.tar;protocol=https;name=fw \
		https://github.com/MorseMicro/bcf_binaries/releases/download/v1.12.4/morsemicro-bcf-rel_1_12_4_2024_Jun_11.tar;protocol=https;name=bcf"

SRC_URI[fw.md5sum] = "4e1dfe4ec6b82ebbbbb943ec26867df7"
SRC_URI[fw.sha256sum] = "1510550a13409c5614fe9d3c3c432eb288427ebdb755d242a1360d684839c174"

SRC_URI[bcf.md5sum] = "214eb0fb57e03b9c9c62933b8a2d4113"
SRC_URI[bcf.sha256sum] = "e403764730aa149e78874135da154bab2a24574308d3f2df88c9b4c125766ae3"

S = "${WORKDIR}"

do_install() {
	install -d ${D}/lib/firmware/
	install -m 0644 ${S}/lib/firmware/morse/mm6108.bin ${D}/lib/firmware/mm6108.bin
	install -m 0644 ${S}/lib/firmware/morse/bcf_mf08651_us.bin ${D}/lib/firmware/bcf_mf08651_us.bin
	ln -s -r ${D}/lib/firmware/bcf_mf08651_us.bin ${D}/lib/firmware/bcf_default.bin
}

FILES:${PN} += "/lib/firmware/bcf_mf08651_us.bin /lib/firmware/mm6108.bin /lib/firmware/bcf_default.bin"
INSANE_SKIP:${PN} = "arch"
